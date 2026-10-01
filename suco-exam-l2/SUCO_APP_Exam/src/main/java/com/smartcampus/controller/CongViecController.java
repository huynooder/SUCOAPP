package com.smartcampus.controller;

import com.smartcampus.service.CongViecService;
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
@RequestMapping("/tasks")
public class CongViecController {

    private final CongViecService congViecService;
    private final DuAnService duAnService;
    private final NguoiDungService nguoiDungService;

    public CongViecController(CongViecService congViecService, DuAnService duAnService,
                              NguoiDungService nguoiDungService) {
        this.congViecService = congViecService;
        this.duAnService = duAnService;
        this.nguoiDungService = nguoiDungService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long maDA, Model model, HttpSession session) {
        model.addAttribute("tasks", maDA != null ? congViecService.findByDuAn(maDA) : congViecService.findAll());
        model.addAttribute("projects", duAnService.findAll());
        model.addAttribute("maDA", maDA);
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "task/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        model.addAttribute("projects", duAnService.findAll());
        model.addAttribute("users", nguoiDungService.findActiveUsers());
        model.addAttribute("mucDos", Arrays.asList("THAP", "TRUNG_BINH", "CAO", "KHAN_CAP"));
        model.addAttribute("statuses", Arrays.asList("CHUA_LAM", "DANG_LAM", "HOAN_THANH", "HUY"));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "task/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long maDA,
                         @RequestParam String tenCV,
                         @RequestParam(required = false) String moTa,
                         @RequestParam(required = false) Long maNguoiLam,
                         @RequestParam(required = false) String mucDo,
                         @RequestParam(required = false) String trangThai,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayHetHan,
                         RedirectAttributes ra) {
        try {
            congViecService.create(maDA, tenCV, moTa, maNguoiLam, mucDo, trangThai, ngayBatDau, ngayHetHan);
            ra.addFlashAttribute("success", "Tạo công việc thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("task", congViecService.findById(id));
        model.addAttribute("projects", duAnService.findAll());
        model.addAttribute("users", nguoiDungService.findActiveUsers());
        model.addAttribute("mucDos", Arrays.asList("THAP", "TRUNG_BINH", "CAO", "KHAN_CAP"));
        model.addAttribute("statuses", Arrays.asList("CHUA_LAM", "DANG_LAM", "HOAN_THANH", "HUY"));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "task/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String tenCV,
                         @RequestParam(required = false) String moTa,
                         @RequestParam(required = false) Long maNguoiLam,
                         @RequestParam(required = false) String mucDo,
                         @RequestParam(required = false) String trangThai,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayHetHan,
                         RedirectAttributes ra) {
        try {
            congViecService.update(id, tenCV, moTa, maNguoiLam, mucDo, trangThai, ngayBatDau, ngayHetHan);
            ra.addFlashAttribute("success", "Cập nhật công việc thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks";
    }
}
