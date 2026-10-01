package com.smartcampus.repository;

import com.smartcampus.entity.CongViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongViecRepository extends JpaRepository<CongViec, Long> {
    List<CongViec> findByDuAn_MaDA(Long maDA);
}
