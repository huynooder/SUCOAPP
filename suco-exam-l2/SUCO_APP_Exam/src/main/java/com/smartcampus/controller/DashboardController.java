package com.smartcampus.controller;

import com.smartcampus.entity.SuCo;
import com.smartcampus.service.SuCoService;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final SuCoService suCoService;

    public DashboardController(SuCoService suCoService) {
        this.suCoService = suCoService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, HttpSession session) {
        Map<String, Long> byStatus = suCoService.thongKeTrangThai();
        Map<String, Long> byProject = suCoService.thongKeDuAn();
        List<SuCo> all = suCoService.findAll();
        long quaHan = all.stream().filter(SuCo::isQuaHan).count();

        model.addAttribute("byStatus", byStatus);
        model.addAttribute("byProject", byProject);
        model.addAttribute("total", all.size());
        model.addAttribute("quaHan", quaHan);
        model.addAttribute("recent", all.stream().limit(10).toList());
        model.addAttribute("currentUser", SessionHelper.getUser(session));
        return "dashboard/index";
    }

    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<SuCo> list = suCoService.findAll();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sự cố");
        String[] headers = {"Mã SC", "Tiêu đề", "Dự án", "Mức độ", "Trạng thái",
                "Người báo cáo", "Người xử lý", "Ngày tạo", "Hạn XL", "Quá hạn",
                "Nguyên nhân", "Giải pháp", "Kết quả"};
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            Cell c = headerRow.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(headerStyle);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int rowIdx = 1;
        for (SuCo s : list) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(s.getMaSC());
            r.createCell(1).setCellValue(s.getTieuDe());
            r.createCell(2).setCellValue(s.getDuAn() != null ? s.getDuAn().getTenDA() : "");
            r.createCell(3).setCellValue(s.getMucDo());
            r.createCell(4).setCellValue(s.getTrangThai());
            r.createCell(5).setCellValue(s.getNguoiBaoCao() != null ? s.getNguoiBaoCao().getHoTen() : "");
            r.createCell(6).setCellValue(s.getNguoiXuLy() != null ? s.getNguoiXuLy().getHoTen() : "");
            r.createCell(7).setCellValue(s.getNgayTao() != null ? s.getNgayTao().format(fmt) : "");
            r.createCell(8).setCellValue(s.getHanXuLy() != null ? s.getHanXuLy().format(fmt) : "");
            r.createCell(9).setCellValue(s.isQuaHan() ? "Có" : "Không");
            r.createCell(10).setCellValue(s.getNguyenNhan() != null ? s.getNguyenNhan() : "");
            r.createCell(11).setCellValue(s.getGiaiPhap() != null ? s.getGiaiPhap() : "");
            r.createCell(12).setCellValue(s.getKetQua() != null ? s.getKetQua() : "");
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        String filename = URLEncoder.encode("bao-cao-su-co.xlsx", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        wb.write(response.getOutputStream());
        wb.close();
    }
}
