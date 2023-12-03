package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connect")
public class CreatorSessionController {

    @GetMapping("/admin")
    public void loginAdmin(HttpServletResponse response) {
        // Définir un cookie pour un admin
        setCookie(response, "userType", "admin");
        setCookie(response, "userEmail", "admin@nextech.com");
    }

    @GetMapping("/client")
    public void loginClient(HttpServletResponse response) {
        // Définir un cookie pour un client
        setCookie(response, "userType", "client");
        setCookie(response, "userEmail", "client@nextech.com");
    }

    @GetMapping("/merchant")
    public void loginMerchant(HttpServletResponse response) {
        // Définir un cookie pour un marchand
        setCookie(response, "userType", "merchant");
        setCookie(response, "userEmail", "merchant@nextech.com");
    }

    private void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        // Définir le chemin du cookie en fonction de votre configuration
        cookie.setPath("/");
        // Vous pouvez également définir d'autres propriétés du cookie si nécessaire
        response.addCookie(cookie);
    }
}
