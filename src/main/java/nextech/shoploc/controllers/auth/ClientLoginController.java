package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.services.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientLoginController {
	
	

    @Autowired
    private ClientService clientService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpSession session) {
        if (sessionManager.isUserConnectedAsClient(session)) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/dashboard");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/login");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        ClientResponseDTO crd = clientService.loginClient(email, password);
        if (crd != null) {
            sessionManager.setUserAsConnected(email, String.valueOf(UserTypes.client), session);
            Map<String, Object> response = new HashMap<>();
            response.put("object", crd);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Identifiant ou mot de passe incorrect");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/register")
    public ResponseEntity<Map<String, Object>> register() {
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/client/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@ModelAttribute("user") ClientRequestDTO client) {
        ClientResponseDTO crd = clientService.createClient(client);
        Map<String, Object> response = new HashMap<>();
        if (crd == null) {
            response.put("error", "L'inscription a échoué. Veuillez réessayer.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("url", "/client/login");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpSession session) {
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("client")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        System.out.println("Logout client...");
        sessionManager.setUserAsDisconnected(session);
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/client/login");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

