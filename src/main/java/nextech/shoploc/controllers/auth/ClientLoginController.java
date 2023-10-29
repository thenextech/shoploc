package nextech.shoploc.controllers.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.services.client.ClientService;
import nextech.shoploc.services.user.UserService;

@RestController
@RequestMapping("/client")
public class ClientLoginController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
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
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
    	ClientResponseDTO crd = clientService.loginClient(email, password);
        if (crd != null) {
        	sessionManager.setUserAsConnected(email, "client", session);
        	return ResponseEntity.ok(crd);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiant ou mot de passe incorrect");
    }

    @GetMapping("/register")
    public String register() {
        return "client/register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute("user") ClientRequestDTO client) {
        ClientResponseDTO crd = clientService.createClient(client);
        if (crd == null) {
        	String errorMessage = "L'inscription a échoué. Veuillez réessayer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:3000/login");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("client")) {
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
