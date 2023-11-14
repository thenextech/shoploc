package nextech.shoploc.controllers.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextech.shoploc.domains.enums.AccountStatus;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.merchant.MerchantService;
import nextech.shoploc.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
@AllArgsConstructor
@NoArgsConstructor
public class MerchantLoginController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailSenderService emailSenderService;

    private static final String LOGIN_ERROR = "Identifiant ou mot de passe incorrect";
    private static final String REGISTER_ERROR = "L'inscription a échoué. Veuillez réessayer.";
    private static final String UNAUTHORIZED_ERROR = "Merci de vous authentifier pour accéder à cette ressource.";
    private static final String VERIFICATION_CODE_ERROR = "Code de vérification incorrect. Veuillez réessayer.";
    private static final String ACCOUNT_STATUS_ERROR = "Votre compte est ";


    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "merchant")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/merchant/dashboard");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/merchant/login");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws MessagingException {
        Map<String, Object> res = new HashMap<>();
        MerchantResponseDTO merchantResponseDTO = merchantService.getMerchantByEmail(email);
        if (merchantResponseDTO != null && userService.verifyPassword(password, merchantResponseDTO.getPassword())) {
            if (merchantResponseDTO.getStatus().equals(AccountStatus.ACTIVE)) {
                // Envoie de code par mail
                String verificationCode = verificationCodeService.generateVerificationCode();
                emailSenderService.sendHtmlEmail(email, verificationCode);
                // COOKIES pour stocker les informations de session
                sessionManager.setUserToVerify(merchantResponseDTO.getId(), UserTypes.merchant.toString(), verificationCode, response);
                res.put("url", "/merchant/verify");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                res.put("error", ACCOUNT_STATUS_ERROR + merchantResponseDTO.getStatus());
                return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
            }
        } else {
            res.put("error", LOGIN_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/register")
    public ResponseEntity<Map<String, Object>> register() {
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/merchant/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@ModelAttribute("merchant") MerchantRequestDTO merchant) {
        merchant.setStatus(AccountStatus.INACTIVE);
        MerchantResponseDTO ard = merchantService.createMerchant(merchant);
        Map<String, Object> response = new HashMap<>();
        if (ard == null) {
            response.put("error", REGISTER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("url", "/merchant/login");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "merchant")) {
            Map<String, Object> response = new HashMap<>();
            UserResponseDTO merchant = userService.getUserById(sessionManager.getConnectedUserId(request));
            response.put("object", merchant);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", UNAUTHORIZED_ERROR);
            response.put("url", "/merchant/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        sessionManager.setUserAsDisconnected(response);
        Map<String, Object> res = new HashMap<>();
        res.put("url", "/merchant/login");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) {
        String savedCode = sessionManager.getVerificationCode(request);
        Map<String, Object> res = new HashMap<>();

        if (code.equals(savedCode)) {
            Long userId = sessionManager.getConnectedUserId(request);
            sessionManager.setUserAsConnected(userId, String.valueOf(UserTypes.merchant), response);
            res.put("url", "/merchant/dashboard");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put("error", VERIFICATION_CODE_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }
}
