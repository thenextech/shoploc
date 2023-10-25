package nextech.shoploc.controllers;

import jakarta.servlet.http.HttpSession;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class LoginAdminController {

    @Autowired
    private AdminService adminService;

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
        if (adminResponseDTO != null && adminResponseDTO.getPassword().equals(password)) {
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
    public String register(@ModelAttribute("user") AdminRequestDTO admin) {
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
