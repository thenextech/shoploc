package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextech.shoploc.domains.enums.Status;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.auth.VerificationCodeService;
import nextech.shoploc.services.merchant.MerchantService;
import nextech.shoploc.services.user.UserService;
import nextech.shoploc.utils.exceptions.EmailAlreadyExistsException;
import nextech.shoploc.utils.exceptions.MerchantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantLoginController {

    private final MerchantService merchantService;
    private final UserService userService;
    private final SessionManager sessionManager;
    private final VerificationCodeService verificationCodeService;
    private final EmailSenderService emailSenderService;

    private static final String LOGIN_ERROR = "Identifiant ou mot de passe incorrect";
    private static final String REGISTER_ERROR = "L'inscription a échoué. Veuillez réessayer.";
    private static final String UNAUTHORIZED_ERROR = "Merci de vous authentifier pour accéder à cette ressource.";
    private static final String VERIFICATION_CODE_ERROR = "Code de vérification incorrect. Veuillez réessayer.";
    private static final String INTEGRITY_ERROR = "Cette adresse e-mail existe déjà.";
    private static final String ACCOUNT_STATUS_ERROR = "Votre compte est ";
    private static final String URL_MERCHANT_DASHBOARD = "/merchant/dashboard";
    private static final String URL_MERCHANT_LOGIN = "/merchant/login";
    private static final String ERROR_KEY = "error";

    @Autowired
    public MerchantLoginController(MerchantService merchantService, UserService userService, SessionManager sessionManager, VerificationCodeService verificationCodeService, EmailSenderService emailSenderService) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.verificationCodeService = verificationCodeService;
        this.emailSenderService = emailSenderService;
    }


    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "merchant")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", URL_MERCHANT_DASHBOARD);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", URL_MERCHANT_LOGIN);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
        try {
            MerchantResponseDTO merchantResponseDTO = merchantService.getMerchantByEmail(email);
            if (merchantResponseDTO != null && userService.verifyPassword(password, merchantResponseDTO.getPassword())) {
                if (merchantResponseDTO.getStatus().equals(Status.ACTIVE)) {
                    // Envoie de code par mail
                    String verificationCode = verificationCodeService.generateVerificationCode();
                    emailSenderService.sendHtmlEmail(email, verificationCode);
                    // COOKIES pour stocker les informations de session
                    sessionManager.setUserToVerify(merchantResponseDTO.getUserId(), UserTypes.merchant.toString(), verificationCode, response);
                    res.put("url", "/merchant/verify");
                    return new ResponseEntity<>(res, HttpStatus.OK);
                } else {
                    res.put(ERROR_KEY, ACCOUNT_STATUS_ERROR + merchantResponseDTO.getStatus());
                    return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
                }
            } else {
                res.put(ERROR_KEY, LOGIN_ERROR);
                return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            res.put(ERROR_KEY, LOGIN_ERROR);
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody MerchantRequestDTO merchant) {
        Map<String, Object> response = new HashMap<>();
        try {
            merchant.setStatus(Status.INACTIVE);
            MerchantResponseDTO ard = merchantService.createMerchant(merchant);
            if (ard == null) {
                response.put(ERROR_KEY, REGISTER_ERROR);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            } else {
                response.put("url", URL_MERCHANT_LOGIN);
                emailSenderService.sendPartnerVerificationEmail("anissahed18@gmail.com", ard);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (EmailAlreadyExistsException e) {
            e.printStackTrace();
            response.put(ERROR_KEY, INTEGRITY_ERROR);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            response.put(ERROR_KEY, REGISTER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpServletRequest request) {
        if (sessionManager.isUserConnected(request, "merchant")) {
            Map<String, Object> response = new HashMap<>();
            MerchantResponseDTO merchant = merchantService.getMerchantById(sessionManager.getConnectedUserId(request));
            response.put("object", merchant);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put(ERROR_KEY, UNAUTHORIZED_ERROR);
            response.put("url", URL_MERCHANT_LOGIN);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        sessionManager.setUserAsDisconnected(response);
        Map<String, Object> res = new HashMap<>();
        res.put("url", URL_MERCHANT_LOGIN);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) {
        String savedCode = sessionManager.getVerificationCode(request);
        Map<String, Object> res = new HashMap<>();

        if (code.equals(savedCode)) {
            Long userId = sessionManager.getConnectedUserId(request);
            sessionManager.setUserAsConnected(userId, String.valueOf(UserTypes.merchant), response);
            res.put("url", URL_MERCHANT_DASHBOARD);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            res.put(ERROR_KEY, VERIFICATION_CODE_ERROR);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/activate/{merchantId}")
    public ResponseEntity<String> activateMerchant(@PathVariable Long merchantId) {
        try {
            merchantService.activateMerchant(merchantId);
            return ResponseEntity.ok("Le compte du marchand a été activé avec succès.");
        } catch (MerchantNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}