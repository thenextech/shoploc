package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connect")
public class CreatorSessionController {


    @GetMapping("/admin")
    public void loginAdmin(HttpServletRequest request) {
        // Ouvrir une session manuellement pour une admin
        HttpSession session = request.getSession(true);
        session.setAttribute("userType", "admin");
        session.setAttribute("userEmail", "admin@nextech.com");
    }

    @GetMapping("/client")
    public void loginClient(HttpServletRequest request) {
        // Ouvrir une session manuellement pour une admin
        HttpSession session = request.getSession(true);
        session.setAttribute("userType", "client");
        session.setAttribute("userEmail", "client@nextech.com");
    }

    @GetMapping("/merchant")
    public void loginMerchant(HttpServletRequest request) {
        // Ouvrir une session manuellement pour une admin
        HttpSession session = request.getSession(true);
        session.setAttribute("userType", "merchant");
        session.setAttribute("userEmail", "merchant@nextech.com");
    }
}