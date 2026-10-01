package com.smartcampus.entity;

public enum MucDoAnhHuong {
    THAP("Thấp"),
    TRUNG_BINH("Trung bình"),
    CAO("Cao"),
    KHAN_CAP("Khẩn cấp");

    private final String label;

    MucDoAnhHuong(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
