package nextech.shoploc.controllers.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SessionManager {

    private static final String USER_ID_ATTRIBUTE = "userId";
    static final String USER_TYPE_ATTRIBUTE = "userType";
    private static final String IS_CONNECTED_ATTRIBUTE = "isConnected";
    private static final String CODE_VERIFICATION_ATTRIBUTE = "codeVerification";
    private static final String NAME_COOKIE_SESSION = "token-access";

    @Value("${jwt.secret}")
    private String jwtSecret;

    public void setUserAsConnected(Long userId, String userType, HttpServletResponse response) {
        Map<String, Object> userDataMap = buildUserDataMap(userId, userType, "true", null);
        addCookie(response, userDataMap, getJwtCookieDurationInSeconds());
    }

    public void setUserToVerify(Long userId, String userType, String verificationCode, HttpServletResponse response) {
        Map<String, Object> userDataMap = buildUserDataMap(userId, userType, "false", verificationCode);
        addCookie(response, userDataMap, getJwtCookieDurationInSeconds());
    }

    public void setUserAsDisconnected(HttpServletResponse response) {
        removeCookie(response);
    }

    public boolean isUserConnected(HttpServletRequest request, String userType) {
        return getClaims(request)
                .map(claims -> claims.get(IS_CONNECTED_ATTRIBUTE, Boolean.class) &&
                        claims.get(USER_TYPE_ATTRIBUTE, String.class).equals(userType))
                .orElse(false);
    }

    public String getVerificationCode(HttpServletRequest request) {
        return getClaims(request)
                .map(claims -> claims.get(CODE_VERIFICATION_ATTRIBUTE, String.class))
                .orElse(null);
    }

    public String getConnectedUserType(HttpServletRequest request) {
        return getClaims(request)
                .map(claims -> claims.get(USER_TYPE_ATTRIBUTE, String.class))
                .orElse(null);
    }

    public Long getConnectedUserId(HttpServletRequest request) {
        return getClaims(request)
                .map(claims -> claims.get(USER_ID_ATTRIBUTE, Long.class))
                .orElse(null);
    }

    public void extendCookieLifeIfNecessary(HttpServletRequest request, HttpServletResponse response) {
        getClaims(request).ifPresent(claims ->
                extendCookieLife(request, response, claims)
        );
    }

    private Optional<Claims> getClaims(HttpServletRequest request) {
        String token = getCookieValue(request);
        return Optional.ofNullable(decodeToken(token));
    }

    private void extendCookieLife(HttpServletRequest request, HttpServletResponse response, Claims claims) {
        Map<String, Object> userDataMap = new HashMap<>();
        userDataMap.put(USER_ID_ATTRIBUTE, claims.get(USER_ID_ATTRIBUTE, Long.class));
        userDataMap.put(USER_TYPE_ATTRIBUTE, claims.get(USER_TYPE_ATTRIBUTE, String.class));
        userDataMap.put(IS_CONNECTED_ATTRIBUTE, claims.get(IS_CONNECTED_ATTRIBUTE, Boolean.class));
        userDataMap.put(CODE_VERIFICATION_ATTRIBUTE, claims.get(CODE_VERIFICATION_ATTRIBUTE, String.class));

        int currentMaxAge = getCookieMaxAge(request);
        int newMaxAge = currentMaxAge + getDurationToAddInSeconds();

        addCookie(response, userDataMap, newMaxAge);
    }

    private void addCookie(HttpServletResponse response, Map<String, Object> userDataMap, int maxAgeInSeconds) {
        String encodedUserData = encodeToken(userDataMap);

        Cookie cookie = new Cookie(NAME_COOKIE_SESSION, encodedUserData);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private int getCookieMaxAge(HttpServletRequest request) {
        return Optional.ofNullable(getCookie(request))
                .map(Cookie::getMaxAge)
                .orElse(0);
    }

    private Map<String, Object> buildUserDataMap(Long userId, String userType, String isConnected, String verificationCode) {
        Map<String, Object> userDataMap = new HashMap<>();
        userDataMap.put(USER_ID_ATTRIBUTE, userId);
        userDataMap.put(USER_TYPE_ATTRIBUTE, userType);
        userDataMap.put(IS_CONNECTED_ATTRIBUTE, Boolean.valueOf(isConnected));
        Optional.ofNullable(verificationCode)
                .ifPresent(code -> userDataMap.put(CODE_VERIFICATION_ATTRIBUTE, code));
        return userDataMap;
    }

    private String encodeToken(Map<String, Object> userDataMap) {
        return Jwts.builder()
                .setClaims(userDataMap)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    private Claims decodeToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SessionManager.NAME_COOKIE_SESSION)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private String getCookieValue(HttpServletRequest request) {
        return Optional.ofNullable(getCookie(request))
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(NAME_COOKIE_SESSION, null);
        cookie.setMaxAge(1);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private int getJwtCookieDurationInSeconds() {
        return 60 * 60 * 4; // durée de vie du cookie en secondes (4 heures)
    }

    private int getDurationToAddInSeconds() {
        return 60 * 60 * 4; // durée de vie du cookie en secondes (4 heures)
    }
}
