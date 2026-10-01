package com.smartcampus.controller;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.entity.SuCo;
import com.smartcampus.entity.TrangThaiSuCo;
import com.smartcampus.service.NguoiDungService;
import com.smartcampus.service.SuCoService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Controller
@RequestMapping("/incidents")
public class IncidentController {

    private final SuCoService suCoService;
    private final NguoiDungService nguoiDungService;

    public IncidentController(SuCoService suCoService, NguoiDungService nguoiDungService) {
        this.suCoService = suCoService;
        this.nguoiDungService = nguoiDungService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long maDA,
                       @RequestParam(required = false) String trangThai,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                       Model model, HttpSession session) {
        LocalDateTime fromDt = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDt = to != null ? to.atTime(LocalTime.MAX) : null;
        model.addAttribute("incidents", suCoService.search(keyword, maDA, trangThai, fromDt, toDt));
        model.addAttribute("duAns", suCoService.getAllDuAn());
        model.addAttribute("trangThais", TrangThaiSuCo.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("maDA", maDA);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "incident/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        model.addAttribute("duAns", suCoService.getAllDuAn());
        model.addAttribute("congViecs", suCoService.getAllCongViec());
        model.addAttribute("mucDos", Arrays.asList("THAP", "TRUNG_BINH", "CAO", "KHAN_CAP"));
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "incident/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String tieuDe,
                         @RequestParam(required = false) String moTa,
                         @RequestParam String mucDo,
                         @RequestParam(required = false) Long maDA,
                         @RequestParam(required = false) Long maCV,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hanXuLy,
                         HttpSession session, RedirectAttributes ra) {
        try {
            NguoiDung user = SessionHelper.getUser(session);
            SuCo s = suCoService.create(tieuDe, moTa, mucDo, maDA, maCV, user, hanXuLy);
            ra.addFlashAttribute("success", "Tạo sự cố " + s.getMaSC() + " thành công");
            return "redirect:/incidents/" + s.getMaSC();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/incidents/create";
        }
    }

    @GetMapping("/{maSC}")
    public String detail(@PathVariable String maSC, Model model, HttpSession session) {
        SuCo s = suCoService.findById(maSC);
        model.addAttribute("incident", s);
        model.addAttribute("timeline", suCoService.getTimeline(maSC));
        model.addAttribute("similar", suCoService.goiYTuongTu(s, 5));
        model.addAttribute("handlers", nguoiDungService.findActiveUsers());
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("allowedNext", TrangThaiSuCo.valueOf(s.getTrangThai()).allowedNext());
        return "incident/detail";
    }

    @PostMapping("/{maSC}/assign")
    public String assign(@PathVariable String maSC, @RequestParam Long maNguoiXuLy, RedirectAttributes ra) {
        try {
            suCoService.assignHandler(maSC, maNguoiXuLy);
            ra.addFlashAttribute("success", "Đã gán người xử lý");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/incidents/" + maSC;
    }

    @PostMapping("/{maSC}/update-status")
    public String updateStatus(@PathVariable String maSC,
                               @RequestParam String trangThaiMoi,
                               @RequestParam(required = false) String ghiChu,
                               @RequestParam(required = false) String nguyenNhan,
                               @RequestParam(required = false) String giaiPhap,
                               @RequestParam(required = false) String ketQua,
                               HttpSession session, RedirectAttributes ra) {
        try {
            NguoiDung user = SessionHelper.getUser(session);
            suCoService.capNhatTrangThai(maSC, trangThaiMoi, user, ghiChu, nguyenNhan, giaiPhap, ketQua);
            ra.addFlashAttribute("success", "Cập nhật trạng thái thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/incidents/" + maSC;
    }
}
