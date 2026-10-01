package com.smartcampus.service;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.repository.NguoiDungRepository;
import com.smartcampus.util.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final NguoiDungRepository nguoiDungRepository;

    public AuthService(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    public NguoiDung login(String email, String matKhau) {
        return nguoiDungRepository.findByEmail(email)
                .filter(u -> u.isActive())
                .filter(u -> Md5Util.matches(matKhau, u.getMatKhau()))
                .orElse(null);
    }

    @Transactional
    public NguoiDung register(String hoTen, String email, String matKhau) {
        if (nguoiDungRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        NguoiDung u = new NguoiDung();
        u.setHoTen(hoTen);
        u.setEmail(email);
        u.setMatKhau(Md5Util.hash(matKhau));
        u.setVaiTro("USER");
        u.setTrangThai(1);
        return nguoiDungRepository.save(u);
    }
}
