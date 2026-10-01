package com.smartcampus.service;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.repository.NguoiDungRepository;
import com.smartcampus.util.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NguoiDungService {

    private final NguoiDungRepository repo;

    public NguoiDungService(NguoiDungRepository repo) {
        this.repo = repo;
    }

    public List<NguoiDung> findAll() {
        return repo.findAll();
    }

    public NguoiDung findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user #" + id));
    }

    public List<NguoiDung> findActiveUsers() {
        return repo.findByVaiTroAndTrangThai("USER", 1);
    }

    @Transactional
    public NguoiDung create(String hoTen, String email, String matKhau, String vaiTro) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        NguoiDung u = new NguoiDung();
        u.setHoTen(hoTen);
        u.setEmail(email);
        u.setMatKhau(Md5Util.hash(matKhau));
        u.setVaiTro(vaiTro != null ? vaiTro : "USER");
        u.setTrangThai(1);
        return repo.save(u);
    }

    @Transactional
    public NguoiDung update(Long id, String hoTen, String email, String vaiTro, Integer trangThai, String matKhauMoi) {
        NguoiDung u = findById(id);
        u.setHoTen(hoTen);
        if (email != null && !email.equals(u.getEmail())) {
            if (repo.existsByEmail(email)) throw new IllegalArgumentException("Email đã tồn tại");
            u.setEmail(email);
        }
        if (vaiTro != null) u.setVaiTro(vaiTro);
        if (trangThai != null) u.setTrangThai(trangThai);
        if (matKhauMoi != null && !matKhauMoi.isBlank()) {
            u.setMatKhau(Md5Util.hash(matKhauMoi));
        }
        return repo.save(u);
    }

    @Transactional
    public void toggleStatus(Long id) {
        NguoiDung u = findById(id);
        u.setTrangThai(u.getTrangThai() == 1 ? 0 : 1);
        repo.save(u);
    }
}
