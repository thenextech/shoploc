package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
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
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password, HttpSession session) {

        AdminResponseDTO adminResponseDTO = adminService.getAdminByEmail(email);
        if (adminResponseDTO != null && userService.verifyPassword(password, adminResponseDTO.getPassword())) {
            // Générez un code de vérification et envoyez-le par e-mail
            String verificationCode = verificationCodeService.generateVerificationCode();
            String subject = "Code de vérification Shoploc";
            String text = "Votre code de vérification Shoploc est : " + verificationCode;
            emailSenderService.sendEmail(email, subject, text);

            // Stockez le code de vérification dans la session
            session.setAttribute("verificationCode", verificationCode);

            // Redirigez l'utilisateur vers la page de vérification
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
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("admin")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/admin/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
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
        String savedCode = (String) session.getAttribute("verificationCode");
        if (code.equals(savedCode)) {
            // Code de vérification valide, accordez une session
            sessionManager.setUserAsConnected(sessionManager.getConnectedUserEmail(session), String.valueOf(UserTypes.admin), session);

            // Redirigez l'utilisateur vers le tableau de bord
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
