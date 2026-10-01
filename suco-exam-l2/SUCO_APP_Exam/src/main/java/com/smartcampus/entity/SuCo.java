package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "SUCO")
public class SuCo {

    @Id
    @Column(name = "MASC", length = 10)
    private String maSC;

    @Column(name = "TIEUDE", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "MOTA", length = 2000)
    private String moTa;

    @Column(name = "MUCDO", nullable = false, length = 20)
    private String mucDo = "TRUNG_BINH";

    @Column(name = "TRANGTHAI", nullable = false, length = 20)
    private String trangThai = "MOI";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MADA")
    private DuAn duAn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MACV")
    private CongViec congViec;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANGUOIBAOCAO", nullable = false)
    private NguoiDung nguoiBaoCao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANGUOIXULY")
    private NguoiDung nguoiXuLy;

    @Column(name = "NGAYTAO")
    private LocalDateTime ngayTao;

    @Column(name = "NGAYBATDAUXL")
    private LocalDateTime ngayBatDauXL;

    @Column(name = "NGAYKETTHUC")
    private LocalDateTime ngayKetThuc;

    @Column(name = "HANXULY")
    private LocalDateTime hanXuLy;

    @Column(name = "NGUYENNHAN", length = 1000)
    private String nguyenNhan;

    @Column(name = "GIAIPHAP", length = 2000)
    private String giaiPhap;

    @Column(name = "KETQUA", length = 1000)
    private String ketQua;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThai == null) trangThai = "MOI";
        if (mucDo == null) mucDo = "TRUNG_BINH";
        if (hanXuLy == null) hanXuLy = ngayTao.plusDays(3);
    }

    public SuCo() {}

    /** Qua han: co HanXuLy, chua dong, va da qua han */
    public boolean isQuaHan() {
        if (hanXuLy == null) return false;
        if ("DONG".equals(trangThai) || "DA_XU_LY".equals(trangThai)) return false;
        return LocalDateTime.now().isAfter(hanXuLy);
    }

    public long soGioConLai() {
        if (hanXuLy == null) return 0;
        return ChronoUnit.HOURS.between(LocalDateTime.now(), hanXuLy);
    }

    // Getters & Setters
    public String getMaSC() { return maSC; }
    public void setMaSC(String maSC) { this.maSC = maSC; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getMucDo() { return mucDo; }
    public void setMucDo(String mucDo) { this.mucDo = mucDo; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public DuAn getDuAn() { return duAn; }
    public void setDuAn(DuAn duAn) { this.duAn = duAn; }
    public CongViec getCongViec() { return congViec; }
    public void setCongViec(CongViec congViec) { this.congViec = congViec; }
    public NguoiDung getNguoiBaoCao() { return nguoiBaoCao; }
    public void setNguoiBaoCao(NguoiDung nguoiBaoCao) { this.nguoiBaoCao = nguoiBaoCao; }
    public NguoiDung getNguoiXuLy() { return nguoiXuLy; }
    public void setNguoiXuLy(NguoiDung nguoiXuLy) { this.nguoiXuLy = nguoiXuLy; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
    public LocalDateTime getNgayBatDauXL() { return ngayBatDauXL; }
    public void setNgayBatDauXL(LocalDateTime ngayBatDauXL) { this.ngayBatDauXL = ngayBatDauXL; }
    public LocalDateTime getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public LocalDateTime getHanXuLy() { return hanXuLy; }
    public void setHanXuLy(LocalDateTime hanXuLy) { this.hanXuLy = hanXuLy; }
    public String getNguyenNhan() { return nguyenNhan; }
    public void setNguyenNhan(String nguyenNhan) { this.nguyenNhan = nguyenNhan; }
    public String getGiaiPhap() { return giaiPhap; }
    public void setGiaiPhap(String giaiPhap) { this.giaiPhap = giaiPhap; }
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
}
