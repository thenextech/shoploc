package nextech.shoploc.controllers.auth;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import nextech.shoploc.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (sessionManager.isUserConnectedAsAdmin(session)) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        AdminResponseDTO adminResponseDTO = adminService.getAdminByEmail(email);
        if (adminResponseDTO != null && userService.verifyPassword(password, adminResponseDTO.getPassword())) {
            sessionManager.setUserAsConnected(email, "admin", session);
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/login?error";
    }

    @GetMapping("/register")
    public String register() {
        return "admin/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("admin") AdminRequestDTO admin) {
        AdminResponseDTO ard = adminService.createAdmin(admin);
        if (ard == null) {
            return "redirect:/admin/register?error";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session != null && sessionManager.getConnectedUserType(session) != null && sessionManager.getConnectedUserEmail(session) != null && sessionManager.getConnectedUserType(session).equals("admin")) {
            return "admin/dashboard";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("Logout admin...");
        sessionManager.setUserAsDisconnected(session);
        return "redirect:/admin/login";
    }
}
