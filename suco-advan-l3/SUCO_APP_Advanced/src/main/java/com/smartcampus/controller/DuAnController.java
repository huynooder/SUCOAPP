package com.smartcampus.controller;

import com.smartcampus.service.DuAnService;
import com.smartcampus.service.NguoiDungService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;

@Controller
@RequestMapping("/projects")
public class DuAnController {

    private final DuAnService duAnService;
    private final NguoiDungService nguoiDungService;

    public DuAnController(DuAnService duAnService, NguoiDungService nguoiDungService) {
        this.duAnService = duAnService;
        this.nguoiDungService = nguoiDungService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        model.addAttribute("projects", duAnService.findAll());
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "project/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        model.addAttribute("users", nguoiDungService.findAll());
        model.addAttribute("statuses", Arrays.asList("CHUAN_BI", "DANG_DIEN_RA", "HOAN_THANH", "HUY"));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "project/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String tenDA,
                         @RequestParam(required = false) String moTa,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
                         @RequestParam(required = false) String trangThai,
                         @RequestParam(required = false) Long maQuanLy,
                         RedirectAttributes ra) {
        try {
            duAnService.create(tenDA, moTa, ngayBatDau, ngayKetThuc, trangThai, maQuanLy);
            ra.addFlashAttribute("success", "Tạo dự án thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("project", duAnService.findById(id));
        model.addAttribute("users", nguoiDungService.findAll());
        model.addAttribute("statuses", Arrays.asList("CHUAN_BI", "DANG_DIEN_RA", "HOAN_THANH", "HUY"));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "project/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String tenDA,
                         @RequestParam(required = false) String moTa,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
                         @RequestParam(required = false) String trangThai,
                         @RequestParam(required = false) Long maQuanLy,
                         RedirectAttributes ra) {
        try {
            duAnService.update(id, tenDA, moTa, ngayBatDau, ngayKetThuc, trangThai, maQuanLy);
            ra.addFlashAttribute("success", "Cập nhật dự án thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }
}
