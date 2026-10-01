package com.smartcampus.controller;

import com.smartcampus.entity.DanhMuc;
import com.smartcampus.repository.DanhMucRepository;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class DanhMucController {

    private final DanhMucRepository repo;

    public DanhMucController(DanhMucRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("items", repo.findAll());
        return "category/list";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long maDM,
                       @RequestParam String tenDM,
                       @RequestParam(required = false) String moTa,
                       @RequestParam(required = false) String mauSac,
                       RedirectAttributes ra) {
        DanhMuc d = maDM != null ? repo.findById(maDM).orElse(new DanhMuc()) : new DanhMuc();
        d.setTenDM(tenDM);
        d.setMoTa(moTa);
        d.setMauSac(mauSac != null && !mauSac.isBlank() ? mauSac : "#64748b");
        d.setTrangThai(1);
        repo.save(d);
        ra.addFlashAttribute("success", "Đã lưu danh mục: " + tenDM);
        return "redirect:/categories";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        repo.findById(id).ifPresent(d -> {
            d.setTrangThai(d.getTrangThai() != null && d.getTrangThai() == 1 ? 0 : 1);
            repo.save(d);
        });
        ra.addFlashAttribute("success", "Đã cập nhật trạng thái danh mục");
        return "redirect:/categories";
    }
}
