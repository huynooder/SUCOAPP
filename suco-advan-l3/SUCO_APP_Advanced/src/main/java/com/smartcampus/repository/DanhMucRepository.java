package com.smartcampus.repository;

import com.smartcampus.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    List<DanhMuc> findByTrangThaiOrderByTenDMAsc(Integer trangThai);
}
