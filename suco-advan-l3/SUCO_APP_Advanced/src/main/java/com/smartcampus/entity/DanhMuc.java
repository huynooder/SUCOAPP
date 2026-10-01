package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DANHMUC")
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MADM")
    private Long maDM;

    @Column(name = "TENDM", nullable = false, length = 100)
    private String tenDM;

    @Column(name = "MOTA", length = 500)
    private String moTa;

    @Column(name = "MAUSAC", length = 20)
    private String mauSac = "#6c757d";

    @Column(name = "TRANGTHAI")
    private Integer trangThai = 1;

    @Column(name = "NGAYTAO")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThai == null) trangThai = 1;
    }

    public DanhMuc() {}

    public Long getMaDM() { return maDM; }
    public void setMaDM(Long maDM) { this.maDM = maDM; }
    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }
    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
