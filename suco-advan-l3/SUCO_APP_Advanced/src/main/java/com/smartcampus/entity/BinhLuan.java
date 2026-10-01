package com.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BINHLUAN")
public class BinhLuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MABL")
    private Long maBL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MASC", nullable = false)
    private SuCo suCo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANGUOIVIET", nullable = false)
    private NguoiDung nguoiViet;

    @Column(name = "NOIDUNG", nullable = false, length = 2000)
    private String noiDung;

    @Column(name = "THOIGIAN")
    private LocalDateTime thoiGian;

    @PrePersist
    public void prePersist() {
        if (thoiGian == null) thoiGian = LocalDateTime.now();
    }

    public BinhLuan() {}

    public Long getMaBL() { return maBL; }
    public void setMaBL(Long maBL) { this.maBL = maBL; }
    public SuCo getSuCo() { return suCo; }
    public void setSuCo(SuCo suCo) { this.suCo = suCo; }
    public NguoiDung getNguoiViet() { return nguoiViet; }
    public void setNguoiViet(NguoiDung nguoiViet) { this.nguoiViet = nguoiViet; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
}
