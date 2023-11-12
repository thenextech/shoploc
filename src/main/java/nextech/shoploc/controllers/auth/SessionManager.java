package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UserTypes;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    public static final String USER_EMAIL_ATTRIBUTE = "userEmail";
    public static final String USER_TYPE_ATTRIBUTE = "userType";
    public static final String IS_CONNECTED_ATTRIBUTE = "isConnected";
    public static final String CODE_VERIFICATION_ATTRIBUTE = "codeVerification";


    public void setUserAsConnected(String userEmail, String userType, HttpSession session) {
        session.setAttribute(USER_EMAIL_ATTRIBUTE, userEmail);
        session.setAttribute(USER_TYPE_ATTRIBUTE, userType);
        session.setAttribute(IS_CONNECTED_ATTRIBUTE, true);
    }

    public void setUserToVerify(String userEmail, String userType,String verificationCode,HttpSession session) {
        session.setAttribute(USER_EMAIL_ATTRIBUTE, userEmail);
        session.setAttribute(USER_TYPE_ATTRIBUTE, userType);
        session.setAttribute(IS_CONNECTED_ATTRIBUTE, false);
        session.setAttribute(CODE_VERIFICATION_ATTRIBUTE, verificationCode);
    }

    public void setUserAsDisconnected(HttpSession session) {
        session.invalidate();
    }

    public boolean isUserConnected(HttpSession session) {
        return session.getAttribute(IS_CONNECTED_ATTRIBUTE) != null;
    }


    public boolean isUserConnectedAsAdmin(HttpSession session) {
        return isUserConnected(session) && UserTypes.admin.toString().equals(session.getAttribute(USER_TYPE_ATTRIBUTE));
    }

    public String getConnectedUserType(HttpSession session) {
        return (String) session.getAttribute(USER_TYPE_ATTRIBUTE);
    }

    public String getConnectedUserEmail(HttpSession session) {
        return (String) session.getAttribute(USER_EMAIL_ATTRIBUTE);
    }

    public boolean isUserConnectedAsClient(HttpSession session) {
        return isUserConnected(session) && UserTypes.client.toString().equals(session.getAttribute(USER_TYPE_ATTRIBUTE));
    }

    public boolean isUserConnectedAsMerchant(HttpSession session) {
        return isUserConnected(session) && UserTypes.merchant.toString().equals(session.getAttribute(USER_TYPE_ATTRIBUTE));
    }

    public String getVerificationCode(HttpSession session) {
        return (String) session.getAttribute(CODE_VERIFICATION_ATTRIBUTE);
    }
}
