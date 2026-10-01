package com.smartcampus.controller;

import com.smartcampus.entity.SuCo;
import com.smartcampus.service.SuCoService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tree")
public class TreeController {

    private final SuCoService suCoService;

    public TreeController(SuCoService suCoService) {
        this.suCoService = suCoService;
    }

    @GetMapping
    public String tree(Model model, HttpSession session) {
        List<SuCo> all = suCoService.findAll();

        // Nhóm theo Dự án → Danh mục → Sự cố (cây quan hệ)
        Map<String, Map<String, List<SuCo>>> tree = new LinkedHashMap<>();
        for (SuCo s : all) {
            String project = s.getDuAn() != null ? s.getDuAn().getTenDA() : "Chưa gắn dự án";
            String cat = s.getDanhMuc() != null ? s.getDanhMuc().getTenDM() : "Chưa phân loại";
            tree.computeIfAbsent(project, k -> new LinkedHashMap<>())
                .computeIfAbsent(cat, k -> new ArrayList<>())
                .add(s);
        }

        // Thống kê liên kết: cùng dự án / cùng danh mục
        Map<String, Long> byProject = all.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDuAn() != null ? s.getDuAn().getTenDA() : "N/A",
                        Collectors.counting()));
        Map<String, Long> byCat = all.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDanhMuc() != null ? s.getDanhMuc().getTenDM() : "N/A",
                        Collectors.counting()));

        model.addAttribute("currentUser", SessionHelper.getUser(session));
        model.addAttribute("tree", tree);
        model.addAttribute("byProject", byProject);
        model.addAttribute("byCat", byCat);
        model.addAttribute("total", all.size());
        return "tree/index";
    }
}
