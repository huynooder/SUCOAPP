package com.smartcampus.repository;

import com.smartcampus.entity.SuCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuCoRepository extends JpaRepository<SuCo, String> {

    List<SuCo> findByTrangThai(String trangThai);

    @Query("""
        SELECT s FROM SuCo s
        WHERE (:keyword IS NULL OR LOWER(s.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:maDA IS NULL OR s.duAn.maDA = :maDA)
          AND (:trangThai IS NULL OR s.trangThai = :trangThai)
          AND (:fromDate IS NULL OR s.ngayTao >= :fromDate)
          AND (:toDate IS NULL OR s.ngayTao <= :toDate)
        ORDER BY s.ngayTao DESC
        """)
    List<SuCo> search(
            @Param("keyword") String keyword,
            @Param("maDA") Long maDA,
            @Param("trangThai") String trangThai,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("SELECT s.trangThai, COUNT(s) FROM SuCo s GROUP BY s.trangThai")
    List<Object[]> countByTrangThai();

    @Query("SELECT s.duAn.tenDA, COUNT(s) FROM SuCo s WHERE s.duAn IS NOT NULL GROUP BY s.duAn.tenDA")
    List<Object[]> countByDuAn();

    List<SuCo> findByDuAn_MaDAAndTrangThai(Long maDA, String trangThai);
}
