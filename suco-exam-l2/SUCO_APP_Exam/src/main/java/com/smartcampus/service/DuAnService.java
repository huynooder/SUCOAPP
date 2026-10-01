package com.smartcampus.service;

import com.smartcampus.entity.DuAn;
import com.smartcampus.entity.NguoiDung;
import com.smartcampus.repository.DuAnRepository;
import com.smartcampus.repository.NguoiDungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DuAnService {

    private final DuAnRepository repo;
    private final NguoiDungRepository nguoiDungRepo;

    public DuAnService(DuAnRepository repo, NguoiDungRepository nguoiDungRepo) {
        this.repo = repo;
        this.nguoiDungRepo = nguoiDungRepo;
    }

    public List<DuAn> findAll() { return repo.findAll(); }

    public DuAn findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dự án #" + id));
    }

    @Transactional
    public DuAn create(String tenDA, String moTa, LocalDate ngayBatDau, LocalDate ngayKetThuc,
                       String trangThai, Long maQuanLy) {
        DuAn d = new DuAn();
        d.setTenDA(tenDA);
        d.setMoTa(moTa);
        d.setNgayBatDau(ngayBatDau);
        d.setNgayKetThuc(ngayKetThuc);
        d.setTrangThai(trangThai != null ? trangThai : "DANG_DIEN_RA");
        if (maQuanLy != null) {
            d.setQuanLy(nguoiDungRepo.findById(maQuanLy).orElse(null));
        }
        return repo.save(d);
    }

    @Transactional
    public DuAn update(Long id, String tenDA, String moTa, LocalDate ngayBatDau,
                       LocalDate ngayKetThuc, String trangThai, Long maQuanLy) {
        DuAn d = findById(id);
        d.setTenDA(tenDA);
        d.setMoTa(moTa);
        d.setNgayBatDau(ngayBatDau);
        d.setNgayKetThuc(ngayKetThuc);
        if (trangThai != null) d.setTrangThai(trangThai);
        if (maQuanLy != null) {
            d.setQuanLy(nguoiDungRepo.findById(maQuanLy).orElse(null));
        }
        return repo.save(d);
    }
}
