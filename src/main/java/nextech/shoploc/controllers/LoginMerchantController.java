package nextech.shoploc.controllers;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.services.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/merchant")
public class LoginMerchantController {

    @Autowired
    private MerchantService clientService;

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
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        MerchantResponseDTO merchantResponseDTO = clientService.getMerchantByEmail(email);
        if (merchantResponseDTO != null &&
                merchantResponseDTO.getPassword().equals(password)) {
            sessionManager.setUserAsConnected(email, "merchant", session);
            return "redirect:/merchant/dashboard";
        }
        return "redirect:/merchant/login?error";
    }

    @GetMapping("/register")
    public String register() {
        return "merchant/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") MerchantResponseDTO merchant) {
        MerchantResponseDTO ard = clientService.createMerchant(merchant);
        if (ard == null) {
            return "redirect:/merchant/register?error";
        }
        return "redirect:/merchant/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session != null
                && sessionManager.getConnectedUserType(session) != null
                && sessionManager.getConnectedUserEmail(session) != null
                && sessionManager.getConnectedUserType(session).equals("merchant")) {
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
