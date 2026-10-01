package com.smartcampus.controller;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.service.NguoiDungService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final NguoiDungService service;

    public UserController(NguoiDungService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        model.addAttribute("users", service.findAll());
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "user/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "user/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String hoTen,
                         @RequestParam String email,
                         @RequestParam String matKhau,
                         @RequestParam String vaiTro,
                         RedirectAttributes ra) {
        try {
            service.create(hoTen, email, matKhau, vaiTro);
            ra.addFlashAttribute("success", "Tạo user thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("user", service.findById(id));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "user/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String hoTen,
                         @RequestParam String email,
                         @RequestParam String vaiTro,
                         @RequestParam Integer trangThai,
                         @RequestParam(required = false) String matKhauMoi,
                         RedirectAttributes ra) {
        try {
            service.update(id, hoTen, email, vaiTro, trangThai, matKhauMoi);
            ra.addFlashAttribute("success", "Cập nhật thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        service.toggleStatus(id);
        ra.addFlashAttribute("success", "Đã đổi trạng thái tài khoản");
        return "redirect:/users";
    }
}
