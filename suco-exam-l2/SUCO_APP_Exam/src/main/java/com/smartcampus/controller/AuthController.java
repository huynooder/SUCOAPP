package com.smartcampus.controller;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.service.AuthService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String matKhau,
                        HttpSession session,
                        RedirectAttributes ra) {
        NguoiDung user = authService.login(email, matKhau);
        if (user == null) {
            ra.addFlashAttribute("error", "Email hoặc mật khẩu không đúng / tài khoản bị khóa");
            return "redirect:/login";
        }
        SessionHelper.setUser(session, user);
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String hoTen,
                           @RequestParam String email,
                           @RequestParam String matKhau,
                           RedirectAttributes ra) {
        try {
            authService.register(hoTen, email, matKhau);
            ra.addFlashAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionHelper.clear(session);
        return "redirect:/login";
    }
}
