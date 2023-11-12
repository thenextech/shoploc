package nextech.shoploc.controllers.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailSenderService emailSenderService;


    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpSession session) {
        if (sessionManager.isUserConnectedAsAdmin(session)) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/admin/dashboard");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/admin/login");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password, HttpSession session) throws MessagingException {
        AdminResponseDTO adminResponseDTO = adminService.getAdminByEmail(email);
        if (adminResponseDTO != null && userService.verifyPassword(password, adminResponseDTO.getPassword())) {
            String verificationCode = verificationCodeService.generateVerificationCode();
            emailSenderService.sendHtmlEmail(email, verificationCode);
            sessionManager.setUserToVerify(email, UserTypes.admin.toString(), session, verificationCode);
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/admin/verify");
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
        response.put("url", "/admin/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@ModelAttribute("admin") AdminRequestDTO admin) {
        AdminResponseDTO ard = adminService.createAdmin(admin);
        Map<String, Object> response = new HashMap<>();
        if (ard == null) {
            response.put("error", "L'inscription a échoué. Veuillez réessayer.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("url", "/admin/login");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpSession session) {
        if (sessionManager.isUserConnectedAsAdmin(session)) {
            Map<String, Object> response = new HashMap<>();
            UserResponseDTO admin = userService.getUserByEmail(sessionManager.getConnectedUserEmail(session));
            response.put("object", admin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Merci de vous authentifier pour accéder à cette ressource.");
            response.put("url", "/admin/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        System.out.println("Logout admin...");
        sessionManager.setUserAsDisconnected(session);
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/admin/login");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code, HttpSession session) {
        String savedCode = sessionManager.getVerificationCode(session);
        if (code.equals(savedCode)) {
            // Code de vérification valide, accorder une session
            sessionManager.setUserAsConnected(sessionManager.getConnectedUserEmail(session), String.valueOf(UserTypes.admin), session);
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/admin/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Code de vérification incorrect, gérer l'erreur
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Code de vérification incorrect. Veuillez réessayer.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

}
