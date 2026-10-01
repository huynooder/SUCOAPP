package com.smartcampus.entity;

import java.util.EnumSet;
import java.util.Set;

/**
 * Trang thai su co - quy dinh chuyen trang thai hop le, khong cho nhay coc.
 * MOI -> DANG_XU_LY -> DA_XU_LY -> DONG
 * Cho phep: MOI -> DONG (huy), DANG_XU_LY -> MOI (tra ve)
 */
public enum TrangThaiSuCo {
    MOI("Mới"),
    DANG_XU_LY("Đang xử lý"),
    DA_XU_LY("Đã xử lý"),
    DONG("Đóng");

    private final String label;

    TrangThaiSuCo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** Cac trang thai co the chuyen den tu trang thai hien tai */
    public Set<TrangThaiSuCo> allowedNext() {
        return switch (this) {
            case MOI -> EnumSet.of(DANG_XU_LY, DONG);
            case DANG_XU_LY -> EnumSet.of(DA_XU_LY, MOI, DONG);
            case DA_XU_LY -> EnumSet.of(DONG, DANG_XU_LY);
            case DONG -> EnumSet.noneOf(TrangThaiSuCo.class);
        };
    }

    public boolean canTransitTo(TrangThaiSuCo next) {
        return allowedNext().contains(next);
    }
}
