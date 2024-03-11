package nextech.shoploc.controllers.auth;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.user.UserService;

@RestController
@RequestMapping("/admin")

public class AdminLoginController {

    private final AdminService adminService;
    private final UserService userService;
    private final SessionManager sessionManager;
    private final VerificationCodeService verificationCodeService;
    private final EmailSenderService emailSenderService;

    private static final String LOGIN_ERROR = "Identifiant ou mot de passe incorrect";
    private static final String REGISTER_ERROR = "L'inscription a échoué. Veuillez réessayer.";
    private static final String UNAUTHORIZED_ERROR = "Merci de vous authentifier pour accéder à cette ressource.";
    private static final String VERIFICATION_CODE_ERROR = "Code de vérification incorrect. Veuillez réessayer.";
    private static final String URL_ADMIN_DASHBOARD = "/admin/dashboard";
    private static final String URL_ADMIN_LOGIN = "/admin/login";
    private static final String ERROR_KEY = "error";

    @Autowired
    public AdminLoginController(final AdminService adminService,
                                final UserService userService,
                                final SessionManager sessionManager,
                                final VerificationCodeService verificationCodeService,
                                final EmailSenderService emailSenderService) {
        this.adminService = adminService;
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.verificationCodeService = verificationCodeService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "admin")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", URL_ADMIN_DASHBOARD);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", URL_ADMIN_LOGIN);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     HttpServletResponse response
    ) throws MessagingException {
        AdminResponseDTO adminResponseDTO = adminService.getAdminByEmail(email);
        if (adminResponseDTO != null && userService.verifyPassword(password, adminResponseDTO.getPassword())) {
            // Envoie de code par mail
            String verificationCode = verificationCodeService.generateVerificationCode();
            emailSenderService.sendHtmlEmail(email, verificationCode);
            // COOKIES pour stocker les informations de session
            sessionManager.setUserToVerify(adminResponseDTO.getUserId(), UserTypes.admin.toString(), verificationCode, response);
            Map<String, Object> res = new HashMap<>();
            res.put("url", "/admin/verify");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put(ERROR_KEY, LOGIN_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/register")
    public ResponseEntity<Map<String, Object>> register() {
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/admin/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AdminRequestDTO admin) {
        AdminResponseDTO ard = adminService.createAdmin(admin);
        Map<String, Object> response = new HashMap<>();
        if (ard == null) {
            response.put(ERROR_KEY, REGISTER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("url", URL_ADMIN_LOGIN);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "admin")) {
            Map<String, Object> response = new HashMap<>();
            UserResponseDTO admin = userService.getUserById(sessionManager.getConnectedUserId(request));
            response.put("object", admin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put(ERROR_KEY, UNAUTHORIZED_ERROR);
            response.put("url", URL_ADMIN_LOGIN);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        sessionManager.setUserAsDisconnected(response);
        Map<String, Object> res = new HashMap<>();
        res.put("url", URL_ADMIN_LOGIN);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code,
                                                      HttpServletResponse response,
                                                      HttpServletRequest request) {
        String savedCode = sessionManager.getVerificationCode(request);
        Map<String, Object> res = new HashMap<>();

        if (code.equals(savedCode)) {
            Long userId = sessionManager.getConnectedUserId(request);
            sessionManager.setUserAsConnected(userId, String.valueOf(UserTypes.admin), response);
            res.put("url", URL_ADMIN_DASHBOARD);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put(ERROR_KEY, VERIFICATION_CODE_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }
}
