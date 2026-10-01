package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LICHSU")
public class LichSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MALS")
    private Long maLS;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MASC", nullable = false)
    private SuCo suCo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANGUOITHUCHIEN", nullable = false)
    private NguoiDung nguoiThucHien;

    @Column(name = "TRANGTHAICU", length = 20)
    private String trangThaiCu;

    @Column(name = "TRANGTHAIMOI", nullable = false, length = 20)
    private String trangThaiMoi;

    @Column(name = "GHICHU", length = 1000)
    private String ghiChu;

    @Column(name = "NGUYENNHAN", length = 1000)
    private String nguyenNhan;

    @Column(name = "GIAIPHAP", length = 2000)
    private String giaiPhap;

    @Column(name = "THOIGIAN")
    private LocalDateTime thoiGian;

    @PrePersist
    public void prePersist() {
        if (thoiGian == null) thoiGian = LocalDateTime.now();
    }

    public LichSu() {}

    public Long getMaLS() { return maLS; }
    public void setMaLS(Long maLS) { this.maLS = maLS; }
    public SuCo getSuCo() { return suCo; }
    public void setSuCo(SuCo suCo) { this.suCo = suCo; }
    public NguoiDung getNguoiThucHien() { return nguoiThucHien; }
    public void setNguoiThucHien(NguoiDung nguoiThucHien) { this.nguoiThucHien = nguoiThucHien; }
    public String getTrangThaiCu() { return trangThaiCu; }
    public void setTrangThaiCu(String trangThaiCu) { this.trangThaiCu = trangThaiCu; }
    public String getTrangThaiMoi() { return trangThaiMoi; }
    public void setTrangThaiMoi(String trangThaiMoi) { this.trangThaiMoi = trangThaiMoi; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public String getNguyenNhan() { return nguyenNhan; }
    public void setNguyenNhan(String nguyenNhan) { this.nguyenNhan = nguyenNhan; }
    public String getGiaiPhap() { return giaiPhap; }
    public void setGiaiPhap(String giaiPhap) { this.giaiPhap = giaiPhap; }
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
}
