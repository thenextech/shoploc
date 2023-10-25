package nextech.shoploc.controllers;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.services.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class LoginClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (sessionManager.isUserConnectedAsClient(session)) {
            return "redirect:/client/dashboard";
        }
        return "client/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        ClientResponseDTO clientResponseDTO = clientService.getClientByEmail(email);
        if (clientResponseDTO != null &&
                clientResponseDTO.getPassword().equals(password)) {
            sessionManager.setUserAsConnected(email, "client", session);
            return "redirect:/client/dashboard";
        }
        return "redirect:/client/login?error";
    }

    @GetMapping("/register")
    public String register() {
        return "client/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") ClientResponseDTO client) {
        ClientResponseDTO ard = clientService.createClient(client);
        if (ard == null) {
            return "redirect:/client/register?error";
        }
        return "redirect:/client/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session != null
                && sessionManager.getConnectedUserType(session) != null
                && sessionManager.getConnectedUserEmail(session) != null
                && sessionManager.getConnectedUserType(session).equals("client")) {
            return "client/dashboard";
        }
        return "redirect:/client/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("Logout client...");
        sessionManager.setUserAsDisconnected(session);
        return "redirect:/client/login";
    }

}
