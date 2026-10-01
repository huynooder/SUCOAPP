package com.smartcampus.controller;

import com.smartcampus.ai.AiSuggestionService;
import com.smartcampus.entity.DanhMuc;
import com.smartcampus.entity.SuCo;
import com.smartcampus.repository.DanhMucRepository;
import com.smartcampus.service.SuCoService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ai")
public class AiController {

    private final AiSuggestionService ai;
    private final SuCoService suCoService;
    private final DanhMucRepository danhMucRepo;

    public AiController(AiSuggestionService ai, SuCoService suCoService, DanhMucRepository danhMucRepo) {
        this.ai = ai;
        this.suCoService = suCoService;
        this.danhMucRepo = danhMucRepo;
    }

    @GetMapping
    public String page(Model model, HttpSession session) {
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("aiStatus", ai.getStatus());
        model.addAttribute("online", ai.isOnlineMode());
        model.addAttribute("incidents", suCoService.findAll());
        model.addAttribute("categories", danhMucRepo.findByTrangThaiOrderByTenDMAsc(1));
        return "ai/index";
    }

    @PostMapping("/suggest")
    public String suggest(@RequestParam String tieuDe,
                          @RequestParam(required = false) String moTa,
                          @RequestParam(required = false) String danhMuc,
                          Model model, HttpSession session) {
        String result = ai.suggestSolution(tieuDe, moTa, danhMuc);
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("aiStatus", ai.getStatus());
        model.addAttribute("online", ai.isOnlineMode());
        model.addAttribute("incidents", suCoService.findAll());
        model.addAttribute("categories", danhMucRepo.findByTrangThaiOrderByTenDMAsc(1));
        model.addAttribute("tieuDe", tieuDe);
        model.addAttribute("moTa", moTa);
        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("result", result);
        return "ai/index";
    }

    @PostMapping("/suggest-from-incident")
    public String fromIncident(@RequestParam String maSC, Model model, HttpSession session) {
        SuCo s = suCoService.findById(maSC);
        String dm = s.getDanhMuc() != null ? s.getDanhMuc().getTenDM() : null;
        String result = ai.suggestSolution(s.getTieuDe(), s.getMoTa(), dm);
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("aiStatus", ai.getStatus());
        model.addAttribute("online", ai.isOnlineMode());
        model.addAttribute("incidents", suCoService.findAll());
        model.addAttribute("categories", danhMucRepo.findByTrangThaiOrderByTenDMAsc(1));
        model.addAttribute("tieuDe", s.getTieuDe());
        model.addAttribute("moTa", s.getMoTa());
        model.addAttribute("danhMuc", dm);
        model.addAttribute("result", result);
        model.addAttribute("selectedMaSC", maSC);
        return "ai/index";
    }

    @PostMapping("/classify")
    public String classify(@RequestParam String tieuDe,
                           @RequestParam(required = false) String moTa,
                           Model model, HttpSession session) {
        List<String> cats = danhMucRepo.findByTrangThaiOrderByTenDMAsc(1)
                .stream().map(DanhMuc::getTenDM).collect(Collectors.toList());
        String suggested = ai.suggestCategory(tieuDe, moTa, cats);
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("aiStatus", ai.getStatus());
        model.addAttribute("online", ai.isOnlineMode());
        model.addAttribute("incidents", suCoService.findAll());
        model.addAttribute("categories", danhMucRepo.findByTrangThaiOrderByTenDMAsc(1));
        model.addAttribute("tieuDe", tieuDe);
        model.addAttribute("moTa", moTa);
        model.addAttribute("classifyResult", suggested);
        return "ai/index";
    }
}
