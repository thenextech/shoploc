package nextech.shoploc.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.client.ClientService;
import nextech.shoploc.services.user.UserService;
import nextech.shoploc.utils.exceptions.NotFoundException;

@RestController
@RequestMapping("/client")
public class ClientLoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpSession session) {
        if (sessionManager.isUserConnectedAsClient(session)) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     HttpSession session) throws MessagingException, NotFoundException {
        try {
        	ClientResponseDTO clientResponseDTO = clientService.getClientByEmail(email);
            if (clientResponseDTO != null && userService.verifyPassword(password, clientResponseDTO.getPassword())) {
                String verificationCode = verificationCodeService.generateVerificationCode();
                emailSenderService.sendHtmlEmail(email, verificationCode);
                session.setAttribute("verificationCode", verificationCode);
                Map<String, Object> response = new HashMap<>();
                response.put("url", "/client/verify");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Identifiant ou mot de passe incorrect");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }        	
        } catch(Exception e) {
        	e.printStackTrace();
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
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpSession session) {
        if (sessionManager.isUserConnectedAsClient(session)) {
            Map<String, Object> response = new HashMap<>();
            UserResponseDTO client = userService.getUserByEmail(sessionManager.getConnectedUserEmail(session));
            response.put("object", client);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Merci de vous authentifier pour accéder à cette ressource.");
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

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code, HttpSession session) {
        String savedCode = sessionManager.getVerificationCode(session);
        if (code.equals(savedCode)) {
            // Code de vérification valide, accorder une session
            sessionManager.setUserAsConnected(sessionManager.getConnectedUserEmail(session), String.valueOf(UserTypes.client), session);
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/client/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Code de vérification incorrect, gérer l'erreur
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Code de vérification incorrect. Veuillez réessayer.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}

