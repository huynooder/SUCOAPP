# BÁO CÁO ĐỒ ÁN / BÀI TẬP LỚN

## HỆ THỐNG QUẢN LÝ SỰ CỐ CNTT (SUCO_APP)

**Phiên bản thi – 5 bảng chính**

---

### 1. Giới thiệu đề tài

Sự cố CNTT (mạng, thiết bị, phần mềm) xảy ra thường xuyên trong trường học / doanh nghiệp. Ghi nhận bằng giấy hoặc chat dễ thất lạc, khó theo dõi trạng thái và lịch sử xử lý.

**SUCO_APP** là hệ thống web giúp:
- Người dùng báo cáo sự cố nhanh
- Admin / người phụ trách gán xử lý và theo dõi
- Quản lý dự án – công việc liên quan đến sự cố
- Lưu timeline mọi thay đổi trạng thái

Xây dựng theo mô hình **MVC** với **Spring Boot**, database **Oracle**, phù hợp bài tập lớn / đồ án nhỏ.

---

### 2. Mục tiêu

| Mục tiêu | Mô tả |
|----------|--------|
| 5 bảng chính | NGUOIDUNG, DUAN, CONGVIEC, SUCO, LICHSU |
| Phân quyền | 2 vai trò: ADMIN và USER |
| Quản lý user | ADMIN CRUD user |
| Sự cố | Tạo, gán, cập nhật trạng thái, timeline |
| Dự án & Công việc | Liên kết với sự cố |
| Đơn giản | Không Knowledge Base / AI, mã hóa MD5 |

---

### 3. Công nghệ sử dụng

| Hạng mục | Lựa chọn | Lý do |
|----------|----------|-------|
| Ngôn ngữ | Java JDK 21 | Yêu cầu bài thi |
| Framework | Spring Boot 3.3 | Web nhanh, convention tốt |
| Kiến trúc | **MVC** | Dễ hiểu, dễ bảo vệ |
| View | Thymeleaf + Bootstrap 5 | Server-side render |
| Database | Oracle XE | Theo lab / yêu cầu môn |
| ORM | Spring Data JPA | Giảm SQL thủ công |
| Mã hóa MK | **MD5** | Đơn giản cho bài thi |
| IDE | IntelliJ IDEA | Hỗ trợ Spring tốt |

**Không dùng Java Swing** vì Swing chỉ cho desktop. Yêu cầu làm **web** → Spring Boot + MVC.

---

### 4. Thiết kế CSDL – 5 bảng chính

#### 4.1. Sơ đồ quan hệ (tóm tắt)

```
NGUOIDUNG ──┬──< DUAN (MaQuanLy)
            ├──< CONGVIEC (MaNguoiLam)
            ├──< SUCO (MaNguoiBaoCao, MaNguoiXuLy)
            └──< LICHSU (MaNguoiThucHien)

DUAN ──< CONGVIEC
DUAN ──< SUCO (tùy chọn)
CONGVIEC ──< SUCO (tùy chọn)
SUCO ──< LICHSU
```

#### 4.2. Mô tả bảng

**1. NGUOIDUNG**

| Cột | Kiểu | Ghi chú |
|-----|------|---------|
| MaND | NUMBER PK Identity | |
| HoTen | NVARCHAR2(100) | NOT NULL |
| Email | VARCHAR2(100) | UNIQUE – dùng đăng nhập |
| MatKhau | VARCHAR2(64) | MD5 |
| VaiTro | VARCHAR2(20) | ADMIN \| USER |
| TrangThai | NUMBER(1) | 1=Active, 0=Khóa |
| NgayTao | TIMESTAMP | |

**2. DUAN**

| Cột | Kiểu | Ghi chú |
|-----|------|---------|
| MaDA | NUMBER PK | |
| TenDA, MoTa | ... | |
| NgayBatDau, NgayKetThuc | DATE | |
| TrangThai | VARCHAR2 | CHUAN_BI / DANG_DIEN_RA / HOAN_THANH / HUY |
| MaQuanLy | FK → NGUOIDUNG | |

**3. CONGVIEC**

| Cột | Kiểu | Ghi chú |
|-----|------|---------|
| MaCV | NUMBER PK | |
| MaDA | FK → DUAN | |
| TenCV, MoTa | ... | |
| MaNguoiLam | FK → NGUOIDUNG | |
| MucDo | VARCHAR2 | THAP / TRUNG_BINH / CAO / KHAN_CAP |
| TrangThai | VARCHAR2 | CHUA_LAM / DANG_LAM / HOAN_THANH / HUY |
| NgayHetHan | DATE | |

**4. SUCO**

| Cột | Kiểu | Ghi chú |
|-----|------|---------|
| MaSC | VARCHAR2(10) PK | SC0001… (Sequence + Trigger) |
| TieuDe, MoTa | ... | |
| MucDo | VARCHAR2 | THAP / TRUNG_BINH / CAO / KHAN_CAP |
| TrangThai | VARCHAR2 | MOI / DANG_XU_LY / DA_XU_LY / DONG |
| MaDA, MaCV | FK (tùy chọn) | |
| MaNguoiBaoCao, MaNguoiXuLy | FK → NGUOIDUNG | |
| NgayTao, HanXuLy, NgayKetThuc | TIMESTAMP | |
| NguyenNhan, GiaiPhap, KetQua | ... | |

**5. LICHSU**

| Cột | Kiểu | Ghi chú |
|-----|------|---------|
| MaLS | NUMBER PK | |
| MaSC | FK → SUCO | |
| MaNguoiThucHien | FK → NGUOIDUNG | |
| TrangThaiCu, TrangThaiMoi | VARCHAR2 | |
| GhiChu, NguyenNhan, GiaiPhap | ... | |
| ThoiGian | TIMESTAMP | |

#### 4.3. Quy tắc chuyển trạng thái sự cố

```
MOI        → DANG_XU_LY | DONG
DANG_XU_LY → DA_XU_LY | DONG
DA_XU_LY   → DONG
DONG       → (không chuyển tiếp)
```

---

### 5. Kiến trúc MVC

```
Browser
   │
   ▼
Controller  (Auth, User, DuAn, CongViec, Incident, Dashboard)
   │
   ▼
Service     (business + @Transactional)
   │
   ▼
Repository  (Spring Data JPA)
   │
   ▼
Oracle DB (5 bảng)
```

- Session lưu user đăng nhập (SessionHelper)
- Interceptor chặn URL chưa login; `/users` chỉ ADMIN

---

### 6. Chức năng đã triển khai

1. Đăng nhập / Đăng ký – MD5, kiểm tra email trùng
2. Quản lý User (ADMIN) – CRUD, vai trò ADMIN/USER
3. CRUD Dự án, Công việc
4. Sự cố: tạo, danh sách, chi tiết, gán xử lý, cập nhật trạng thái + timeline
5. Dashboard thống kê theo trạng thái, cảnh báo quá hạn
6. Xuất Excel (danh sách sự cố)

---

### 7. Hướng dẫn cài đặt và chạy

#### 7.1. Database (SQL Developer)

1. Tạo user `smartcampus` / `smartcampus`
2. Chạy file `sql/00_schema_5bang.sql` (F5)
3. Kiểm tra 5 bảng + dữ liệu mẫu

#### 7.2. IntelliJ IDEA

1. Open project (folder có `pom.xml`)
2. SDK = JDK 21
3. Sửa `application.properties` (URL, user, pass Oracle)
4. Run `com.smartcampus.SmartCampusApplication`
5. http://localhost:8080

#### 7.3. Tài khoản mẫu

| Email | Mật khẩu | Vai trò |
|-------|----------|---------|
| admin@campus.edu | 123456 | ADMIN |
| user1@campus.edu | 123456 | USER |
| user2@campus.edu | 123456 | USER |

---

### 8. Cấu trúc thư mục

```
SUCO_APP_Exam/
├── pom.xml
├── README.md
├── sql/00_schema_5bang.sql
├── docs/BAO_CAO_SUCO_APP.md
└── src/main/
    ├── java/com/smartcampus/
    │   ├── SmartCampusApplication.java
    │   ├── config/
    │   ├── controller/
    │   ├── entity/          (5 entity)
    │   ├── repository/
    │   ├── service/
    │   └── util/Md5Util, SessionHelper
    └── resources/
        ├── application.properties
        └── templates/ (auth, dashboard, incident, project, task, user, layout)
```

---

### 9. Kết luận

**Đã đạt:**
- Web (không Swing), Spring Boot MVC, Oracle, JDK 21
- Đúng **5 bảng chính**: Người dùng, Dự án, Công việc, Sự cố, Lịch sử
- Đăng nhập + quản lý user 2 vai trò
- Vòng đời sự cố + timeline
- Code rõ ràng, dễ bảo vệ bài thi

**Hướng mở rộng (bản Full):**
- i18n đa ngôn ngữ
- REST API + frontend React/Vue/…
- BCrypt, Spring Security
- Knowledge Base (nếu có thời gian)

---

### 10. Tài liệu tham khảo

- Spring Boot Reference Documentation
- Oracle Database JDBC Developer’s Guide
- Thymeleaf Documentation

---

**Sinh viên thực hiện:** [Điền tên nhóm]  
**Năm học:** 2025–2026
