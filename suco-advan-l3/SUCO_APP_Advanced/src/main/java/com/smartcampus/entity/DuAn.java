package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DUAN")
public class DuAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MADA")
    private Long maDA;

    @Column(name = "TENDA", nullable = false, length = 200)
    private String tenDA;

    @Column(name = "MOTA", length = 1000)
    private String moTa;

    @Column(name = "NGAYBATDAU")
    private LocalDate ngayBatDau;

    @Column(name = "NGAYKETTHUC")
    private LocalDate ngayKetThuc;

    @Column(name = "TRANGTHAI", nullable = false, length = 20)
    private String trangThai = "DANG_DIEN_RA";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MAQUANLY")
    private NguoiDung quanLy;

    @Column(name = "NGAYTAO")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThai == null) trangThai = "DANG_DIEN_RA";
    }

    public DuAn() {}

    public Long getMaDA() { return maDA; }
    public void setMaDA(Long maDA) { this.maDA = maDA; }
    public String getTenDA() { return tenDA; }
    public void setTenDA(String tenDA) { this.tenDA = tenDA; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public NguoiDung getQuanLy() { return quanLy; }
    public void setQuanLy(NguoiDung quanLy) { this.quanLy = quanLy; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
