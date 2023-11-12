package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UserTypes;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    public void setUserAsConnected(String userEmail, String userType, HttpSession session) {
        session.setAttribute("userEmail", userEmail);
        session.setAttribute("userType", userType);
    }

    public void setUserAsDisconnected(HttpSession session) {
        session.invalidate();
    }

    public boolean isUserConnectedAsAdmin(HttpSession session) {
        return session.getAttribute("userEmail") != null && session.getAttribute("userType").equals(UserTypes.admin);
    }

    public String getConnectedUserType(HttpSession session) {
        return (String) session.getAttribute("userType");
    }

    public String getConnectedUserEmail(HttpSession session) {
        return (String) session.getAttribute("userEmail");
    }

    public boolean isUserConnectedAsClient(HttpSession session) {
        return session.getAttribute("userEmail") != null && session.getAttribute("userType").equals(UserTypes.client);
    }

    public boolean isUserConnectedAsMerchant(HttpSession session) {
        return session.getAttribute("userEmail") != null && session.getAttribute("userType").equals(UserTypes.merchant);
    }



}