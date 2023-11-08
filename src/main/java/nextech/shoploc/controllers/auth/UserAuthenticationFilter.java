package nextech.shoploc.controllers.auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UrlToProtect;
import nextech.shoploc.domains.enums.UserTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@WebFilter(urlPatterns = {"/*"})
public class UserAuthenticationFilter implements Filter {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.endsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }
        if (isUrlToProtect(requestURI)) {
            if (session == null || session.getAttribute("userType") == null) {
                // L'utilisateur n'est pas authentifié, rediriger vers la page de connexion du client
                String loginUrl = "/client/login";
                httpResponse.sendRedirect(httpRequest.getContextPath() + loginUrl);
            } else {
                String userType = sessionManager.getConnectedUserType(session);

                // Vérifier si l'utilisateur peut accéder à cette URL
                if (canUserAccessURL(userType, requestURI, (HttpServletRequest) request)) {
                    chain.doFilter(request, response);
                } else {
                    String loginUrl = "/" + userType + "/login";
                    httpResponse.sendRedirect(httpRequest.getContextPath() + loginUrl);
                }
            }
        } else {
            // Si l'URL n'est pas protégée, laisser passer la requête
            chain.doFilter(request, response);
        }
    }

    private boolean canUserAccessURL(String userType, String requestURI, HttpServletRequest httpRequest) {
        if (UserTypes.admin.toString().equalsIgnoreCase(userType)) {
            // Les administrateurs ont accès à tout
            return true;
        } else if (UserTypes.client.toString().equalsIgnoreCase(userType)) {
            // Les clients ont accès aux URL client
            return requestURI.startsWith(httpRequest.getContextPath() + "/clients/");
        } else if (UserTypes.merchant.toString().equalsIgnoreCase(userType)) {
            // Les marchands ont accès aux URL merchant
            return requestURI.startsWith(httpRequest.getContextPath() + "/merchants/");
        }
        return false;
    }



    @Override
    public void destroy() {
        // Nettoyer le filtre
    }

    private boolean isValidUserType(String userType) {
        return userType != null && (userType.equals("admin") || userType.equals("merchant") || userType.equals("client"));
    }

    private boolean isUrlToProtect(String url) {
        return Arrays.stream(UrlToProtect.values()).anyMatch(urlToProtect -> url.startsWith(urlToProtect.getUrl()));
    }
}
