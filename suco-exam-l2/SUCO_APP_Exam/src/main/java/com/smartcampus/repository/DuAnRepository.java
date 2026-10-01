package com.smartcampus.repository;

import com.smartcampus.entity.DuAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuAnRepository extends JpaRepository<DuAn, Long> {
}
