package com.smartcampus.service;

import com.smartcampus.entity.CongViec;
import com.smartcampus.entity.DuAn;
import com.smartcampus.repository.CongViecRepository;
import com.smartcampus.repository.DuAnRepository;
import com.smartcampus.repository.NguoiDungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CongViecService {

    private final CongViecRepository repo;
    private final DuAnRepository duAnRepo;
    private final NguoiDungRepository nguoiDungRepo;

    public CongViecService(CongViecRepository repo, DuAnRepository duAnRepo,
                           NguoiDungRepository nguoiDungRepo) {
        this.repo = repo;
        this.duAnRepo = duAnRepo;
        this.nguoiDungRepo = nguoiDungRepo;
    }

    public List<CongViec> findAll() { return repo.findAll(); }

    public CongViec findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công việc #" + id));
    }

    public List<CongViec> findByDuAn(Long maDA) { return repo.findByDuAn_MaDA(maDA); }

    @Transactional
    public CongViec create(Long maDA, String tenCV, String moTa, Long maNguoiLam,
                           String mucDo, String trangThai, LocalDate ngayBatDau, LocalDate ngayHetHan) {
        DuAn da = duAnRepo.findById(maDA)
                .orElseThrow(() -> new IllegalArgumentException("Dự án không tồn tại"));
        CongViec cv = new CongViec();
        cv.setDuAn(da);
        cv.setTenCV(tenCV);
        cv.setMoTa(moTa);
        if (maNguoiLam != null) {
            cv.setNguoiLam(nguoiDungRepo.findById(maNguoiLam).orElse(null));
        }
        cv.setMucDo(mucDo != null ? mucDo : "TRUNG_BINH");
        cv.setTrangThai(trangThai != null ? trangThai : "CHUA_LAM");
        cv.setNgayBatDau(ngayBatDau);
        cv.setNgayHetHan(ngayHetHan);
        return repo.save(cv);
    }

    @Transactional
    public CongViec update(Long id, String tenCV, String moTa, Long maNguoiLam,
                           String mucDo, String trangThai, LocalDate ngayBatDau, LocalDate ngayHetHan) {
        CongViec cv = findById(id);
        cv.setTenCV(tenCV);
        cv.setMoTa(moTa);
        if (maNguoiLam != null) {
            cv.setNguoiLam(nguoiDungRepo.findById(maNguoiLam).orElse(null));
        }
        if (mucDo != null) cv.setMucDo(mucDo);
        if (trangThai != null) cv.setTrangThai(trangThai);
        cv.setNgayBatDau(ngayBatDau);
        cv.setNgayHetHan(ngayHetHan);
        return repo.save(cv);
    }
}
