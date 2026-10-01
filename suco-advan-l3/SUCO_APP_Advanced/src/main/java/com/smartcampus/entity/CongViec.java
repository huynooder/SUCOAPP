package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONGVIEC")
public class CongViec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MACV")
    private Long maCV;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MADA", nullable = false)
    private DuAn duAn;

    @Column(name = "TENCV", nullable = false, length = 200)
    private String tenCV;

    @Column(name = "MOTA", length = 1000)
    private String moTa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANGUOILAM")
    private NguoiDung nguoiLam;

    @Column(name = "MUCDO", nullable = false, length = 20)
    private String mucDo = "TRUNG_BINH";

    @Column(name = "TRANGTHAI", nullable = false, length = 20)
    private String trangThai = "CHUA_LAM";

    @Column(name = "NGAYBATDAU")
    private LocalDate ngayBatDau;

    @Column(name = "NGAYHETHAN")
    private LocalDate ngayHetHan;

    @Column(name = "NGAYTAO")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (mucDo == null) mucDo = "TRUNG_BINH";
        if (trangThai == null) trangThai = "CHUA_LAM";
    }

    public CongViec() {}

    public Long getMaCV() { return maCV; }
    public void setMaCV(Long maCV) { this.maCV = maCV; }
    public DuAn getDuAn() { return duAn; }
    public void setDuAn(DuAn duAn) { this.duAn = duAn; }
    public String getTenCV() { return tenCV; }
    public void setTenCV(String tenCV) { this.tenCV = tenCV; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public NguoiDung getNguoiLam() { return nguoiLam; }
    public void setNguoiLam(NguoiDung nguoiLam) { this.nguoiLam = nguoiLam; }
    public String getMucDo() { return mucDo; }
    public void setMucDo(String mucDo) { this.mucDo = mucDo; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(LocalDate ngayHetHan) { this.ngayHetHan = ngayHetHan; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
