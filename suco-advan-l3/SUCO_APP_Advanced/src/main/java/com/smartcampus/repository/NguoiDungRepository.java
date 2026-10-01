package com.smartcampus.repository;

import com.smartcampus.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByEmail(String email);
    boolean existsByEmail(String email);
    List<NguoiDung> findByVaiTro(String vaiTro);
    List<NguoiDung> findByVaiTroAndTrangThai(String vaiTro, Integer trangThai);
}
