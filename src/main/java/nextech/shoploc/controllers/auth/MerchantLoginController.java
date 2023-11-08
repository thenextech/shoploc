package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.domains.enums.UserTypes;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
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
public class MerchantLoginController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpSession session) {
        if (sessionManager.isUserConnectedAsMerchant(session)) {
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
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        MerchantResponseDTO merchantResponseDTO = merchantService.getMerchantByEmail(email);
        if (merchantResponseDTO != null && userService.verifyPassword(password, merchantResponseDTO.getPassword())) {
            sessionManager.setUserAsConnected(email, String.valueOf(UserTypes.merchant), session);
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/merchant/dashboard");
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
        response.put("url", "/merchant/register");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/registerMerchant")
    public ResponseEntity<Map<String, Object>> registerMerchant(@ModelAttribute("merchant") MerchantRequestDTO merchant) {
        MerchantResponseDTO mrd = merchantService.createMerchant(merchant);
        Map<String, Object> response = new HashMap<>();
        if (mrd == null) {
            response.put("error", "L'inscription a échoué. Veuillez réessayer.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("url", "/merchant/login");
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(HttpSession session) {
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("merchant")) {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/merchant/dashboard");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("url", "/merchant/login");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        System.out.println("Logout merchant...");
        sessionManager.setUserAsDisconnected(session);
        Map<String, Object> response = new HashMap<>();
        response.put("url", "/merchant/login");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
