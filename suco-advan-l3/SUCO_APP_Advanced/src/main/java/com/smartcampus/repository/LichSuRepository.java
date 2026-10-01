package com.smartcampus.repository;

import com.smartcampus.entity.LichSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuRepository extends JpaRepository<LichSu, Long> {
    List<LichSu> findBySuCo_MaSCOrderByThoiGianAsc(String maSC);
}
