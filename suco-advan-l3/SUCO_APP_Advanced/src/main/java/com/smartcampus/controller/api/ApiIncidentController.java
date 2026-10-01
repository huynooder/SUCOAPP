package com.smartcampus.controller.api;

import com.smartcampus.entity.SuCo;
import com.smartcampus.service.SuCoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API stub – dùng khi mở rộng mobile app / SPA / AI.
 * Phiên bản thi dùng Thymeleaf MVC, không bắt buộc dùng API này.
 */
@RestController
@RequestMapping("/api/incidents")
public class ApiIncidentController {

    private final SuCoService suCoService;

    public ApiIncidentController(SuCoService suCoService) {
        this.suCoService = suCoService;
    }

    @GetMapping
    public ResponseEntity<List<SuCo>> list() {
        return ResponseEntity.ok(suCoService.findAll());
    }

    @GetMapping("/{maSC}")
    public ResponseEntity<SuCo> get(@PathVariable String maSC) {
        try {
            SuCo s = suCoService.findById(maSC);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
