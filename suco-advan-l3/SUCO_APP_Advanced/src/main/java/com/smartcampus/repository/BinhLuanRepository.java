package com.smartcampus.repository;

import com.smartcampus.entity.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    List<BinhLuan> findBySuCo_MaSCOrderByThoiGianAsc(String maSC);
}
