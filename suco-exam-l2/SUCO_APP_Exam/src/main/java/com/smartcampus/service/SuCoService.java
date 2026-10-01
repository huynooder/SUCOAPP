package com.smartcampus.service;

import com.smartcampus.entity.*;
import com.smartcampus.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuCoService {

    private final SuCoRepository suCoRepo;
    private final DuAnRepository duAnRepo;
    private final CongViecRepository congViecRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final LichSuRepository lichSuRepo;

    public SuCoService(SuCoRepository suCoRepo, DuAnRepository duAnRepo,
                       CongViecRepository congViecRepo, NguoiDungRepository nguoiDungRepo,
                       LichSuRepository lichSuRepo) {
        this.suCoRepo = suCoRepo;
        this.duAnRepo = duAnRepo;
        this.congViecRepo = congViecRepo;
        this.nguoiDungRepo = nguoiDungRepo;
        this.lichSuRepo = lichSuRepo;
    }

    public List<SuCo> findAll() { return suCoRepo.findAll(); }

    public SuCo findById(String maSC) {
        return suCoRepo.findById(maSC)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự cố " + maSC));
    }

    public List<DuAn> getAllDuAn() { return duAnRepo.findAll(); }
    public List<CongViec> getCongViecByDuAn(Long maDA) { return congViecRepo.findByDuAn_MaDA(maDA); }
    public List<CongViec> getAllCongViec() { return congViecRepo.findAll(); }

    @Transactional
    public SuCo create(String tieuDe, String moTa, String mucDo, Long maDA, Long maCV,
                       NguoiDung nguoiBaoCao, LocalDateTime hanXuLy) {
        if (tieuDe == null || tieuDe.isBlank()) {
            throw new IllegalArgumentException("Tiêu đề không được để trống");
        }
        SuCo s = new SuCo();
        long next = suCoRepo.count() + 1;
        s.setMaSC(String.format("SC%04d", next));
        s.setTieuDe(tieuDe.trim());
        s.setMoTa(moTa);
        s.setMucDo(mucDo != null ? mucDo : "TRUNG_BINH");
        s.setTrangThai("MOI");
        s.setNguoiBaoCao(nguoiBaoCao);
        if (maDA != null) {
            s.setDuAn(duAnRepo.findById(maDA).orElse(null));
        }
        if (maCV != null) {
            s.setCongViec(congViecRepo.findById(maCV).orElse(null));
        }
        s.setHanXuLy(hanXuLy != null ? hanXuLy : LocalDateTime.now().plusDays(3));
        s = suCoRepo.save(s);

        LichSu ls = new LichSu();
        ls.setSuCo(s);
        ls.setNguoiThucHien(nguoiBaoCao);
        ls.setTrangThaiCu(null);
        ls.setTrangThaiMoi("MOI");
        ls.setGhiChu("Tạo sự cố mới");
        lichSuRepo.save(ls);
        return s;
    }

    @Transactional
    public SuCo assignHandler(String maSC, Long maNguoiXuLy) {
        SuCo s = findById(maSC);
        NguoiDung handler = nguoiDungRepo.findById(maNguoiXuLy)
                .orElseThrow(() -> new IllegalArgumentException("Người xử lý không tồn tại"));
        s.setNguoiXuLy(handler);
        return suCoRepo.save(s);
    }

    @Transactional
    public SuCo capNhatTrangThai(String maSC, String trangThaiMoi, NguoiDung nguoiThucHien,
                                 String ghiChu, String nguyenNhan, String giaiPhap, String ketQua) {
        SuCo s = findById(maSC);
        TrangThaiSuCo current = TrangThaiSuCo.valueOf(s.getTrangThai());
        TrangThaiSuCo next = TrangThaiSuCo.valueOf(trangThaiMoi);

        if (!current.canTransitTo(next)) {
            throw new IllegalArgumentException(
                    "Không được chuyển từ " + current.getLabel() + " sang " + next.getLabel());
        }

        String cu = s.getTrangThai();
        s.setTrangThai(trangThaiMoi);

        if ("DANG_XU_LY".equals(trangThaiMoi) && s.getNgayBatDauXL() == null) {
            s.setNgayBatDauXL(LocalDateTime.now());
        }
        if ("DA_XU_LY".equals(trangThaiMoi) || "DONG".equals(trangThaiMoi)) {
            s.setNgayKetThuc(LocalDateTime.now());
        }
        if (nguyenNhan != null && !nguyenNhan.isBlank()) s.setNguyenNhan(nguyenNhan);
        if (giaiPhap != null && !giaiPhap.isBlank()) s.setGiaiPhap(giaiPhap);
        if (ketQua != null && !ketQua.isBlank()) s.setKetQua(ketQua);
        s = suCoRepo.save(s);

        LichSu ls = new LichSu();
        ls.setSuCo(s);
        ls.setNguoiThucHien(nguoiThucHien);
        ls.setTrangThaiCu(cu);
        ls.setTrangThaiMoi(trangThaiMoi);
        ls.setGhiChu(ghiChu);
        ls.setNguyenNhan(nguyenNhan);
        ls.setGiaiPhap(giaiPhap);
        lichSuRepo.save(ls);
        return s;
    }

    public List<LichSu> getTimeline(String maSC) {
        return lichSuRepo.findBySuCo_MaSCOrderByThoiGianAsc(maSC);
    }

    public List<SuCo> search(String keyword, Long maDA, String trangThai,
                             LocalDateTime from, LocalDateTime to) {
        String kw = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        return suCoRepo.search(kw, maDA, trangThai, from, to);
    }

    public List<Map.Entry<SuCo, Integer>> goiYTuongTu(SuCo source, int limit) {
        if (source.getDuAn() == null) return List.of();
        List<SuCo> candidates = suCoRepo.findByDuAn_MaDAAndTrangThai(
                source.getDuAn().getMaDA(), "DONG");
        Set<String> keywords = tokenize(source.getTieuDe());
        List<Map.Entry<SuCo, Integer>> scored = new ArrayList<>();
        for (SuCo c : candidates) {
            if (c.getMaSC().equals(source.getMaSC())) continue;
            int score = 0;
            for (String kw : tokenize(c.getTieuDe())) {
                if (keywords.contains(kw)) score++;
            }
            if (score > 0) scored.add(Map.entry(c, score));
        }
        scored.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return scored.stream().limit(limit).collect(Collectors.toList());
    }

    private Set<String> tokenize(String text) {
        if (text == null) return Set.of();
        return Arrays.stream(text.toLowerCase().split("[\\s,.;:!?]+"))
                .filter(w -> w.length() > 2)
                .collect(Collectors.toSet());
    }

    public Map<String, Long> thongKeTrangThai() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : suCoRepo.countByTrangThai()) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    public Map<String, Long> thongKeDuAn() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : suCoRepo.countByDuAn()) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }
}
