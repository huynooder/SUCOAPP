package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NGUOIDUNG")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAND")
    private Long maND;

    @Column(name = "HOTEN", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "MATKHAU", nullable = false, length = 64)
    private String matKhau;

    @Column(name = "VAITRO", nullable = false, length = 20)
    private String vaiTro; // ADMIN | USER

    @Column(name = "TRANGTHAI", nullable = false)
    private Integer trangThai = 1; // 1=Active, 0=Khoa

    @Column(name = "NGAYTAO")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThai == null) trangThai = 1;
    }

    public NguoiDung() {}

    public Long getMaND() { return maND; }
    public void setMaND(Long maND) { this.maND = maND; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public boolean isActive() { return trangThai != null && trangThai == 1; }
    public boolean isAdmin() { return "ADMIN".equals(vaiTro); }
}
