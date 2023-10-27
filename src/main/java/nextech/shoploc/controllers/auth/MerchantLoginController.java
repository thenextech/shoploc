package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.services.merchant.MerchantService;
import nextech.shoploc.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String login(HttpSession session) {
        if (sessionManager.isUserConnectedAsMerchant(session)) {
            return "redirect:/merchant/dashboard";
        }
        return "merchant/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        MerchantResponseDTO merchantResponseDTO = merchantService.getMerchantByEmail(email);
        if (merchantResponseDTO != null && userService.verifyPassword(password, merchantResponseDTO.getPassword())) {
            sessionManager.setUserAsConnected(email, "merchant", session);
            return "redirect:/merchant/dashboard";
        }
        return "redirect:/merchant/login?error";
    }

    @GetMapping("/register")
    public String register() {
        return "merchant/register";
    }

    @PostMapping("/registerMerchant") // Renommée en "registerMerchant"
    public String registerMerchant(@ModelAttribute("user") MerchantRequestDTO merchant) {
        MerchantResponseDTO mrd = merchantService.createMerchant(merchant);
        if (mrd == null) {
            return "redirect:/merchant/register?error";
        }
        return "redirect:/merchant/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("merchant")) {
            return "merchant/dashboard";
        }
        return "redirect:/merchant/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("Logout merchant...");
        sessionManager.setUserAsDisconnected(session);
        return "redirect:/merchant/login";
    }
}
