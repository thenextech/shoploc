package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.client.ClientService;
import nextech.shoploc.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientLoginController {

    private final ClientService clientService;
    private final UserService userService;
    private final SessionManager sessionManager;
    private final VerificationCodeService verificationCodeService;
    private final EmailSenderService emailSenderService;

    private static final String LOGIN_ERROR = "Identifiant ou mot de passe incorrect";
    private static final String REGISTER_ERROR = "L'inscription a échoué. Veuillez réessayer.";
    private static final String INTEGRITY_ERROR = "Cette adresse e-mail existe déjà.";
    private static final String UNAUTHORIZED_ERROR = "Merci de vous authentifier pour accéder à cette ressource.";
    private static final String VERIFICATION_CODE_ERROR = "Code de vérification incorrect. Veuillez réessayer.";

    @Autowired
    public ClientLoginController(final ClientService clientService,
                                 final UserService userService,
                                 final SessionManager sessionManager,
                                 final VerificationCodeService verificationCodeService,
                                 final EmailSenderService emailSenderService) {
        this.clientService = clientService;
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.verificationCodeService = verificationCodeService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "client")) {
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
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     HttpServletResponse response
    ) {
        try {
            ClientResponseDTO clientResponseDTO = clientService.getClientByEmail(email);
            if (clientResponseDTO != null && userService.verifyPassword(password, clientResponseDTO.getPassword())) {
                // Envoie de code par mail
                String verificationCode = verificationCodeService.generateVerificationCode();
                //emailSenderService.sendHtmlEmail(email, verificationCode);
                // COOKIES pour stocker les informations de session
                sessionManager.setUserToVerify(clientResponseDTO.getUserId(), UserTypes.client.toString(), verificationCode, response);
                Map<String, Object> res = new HashMap<>();
                res.put("url", "/client/verify");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                Map<String, Object> res = new HashMap<>();
                res.put("error", LOGIN_ERROR);
                return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("error", LOGIN_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/register")
    public ResponseEntity<Map<String, Object>> register() {
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/client/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody ClientRequestDTO client) {
        Map<String, Object> response = new HashMap<>();
        try {
            ClientResponseDTO ard = clientService.createClient(client);
            if (ard == null) {
                response.put("error", REGISTER_ERROR);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                response.put("url", "/client/login");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            response.put("error", INTEGRITY_ERROR);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "client")) {
            Map<String, Object> response = new HashMap<>();
            UserResponseDTO client = userService.getUserById(sessionManager.getConnectedUserId(request));
            response.put("object", client);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", UNAUTHORIZED_ERROR);
            response.put("url", "/client/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        sessionManager.setUserAsDisconnected(response);
        Map<String, Object> res = new HashMap<>();
        res.put("url", "/client/login");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code,
                                                      HttpServletResponse response,
                                                      HttpServletRequest request) {
        String savedCode = sessionManager.getVerificationCode(request);
        Map<String, Object> res = new HashMap<>();

        if (true) {
            Long userId = sessionManager.getConnectedUserId(request);
            sessionManager.setUserAsConnected(userId, String.valueOf(UserTypes.client), response);
            res.put("url", "/client/dashboard");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put("error", VERIFICATION_CODE_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }
}