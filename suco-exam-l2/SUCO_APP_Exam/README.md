# SUCO_APP Exam – Hệ thống quản lý sự cố CNTT (Smart Campus)

## 1. Giới thiệu

**SUCO_APP Exam** là hệ thống **web** quản lý sự cố CNTT trong môi trường trường học/campus:

- Báo cáo sự cố, gán người xử lý, theo dõi **tiến độ** (trạng thái MOI → ĐANG_XỬ_LÝ → ĐÃ_XỬ_LÝ → ĐÓNG)
- **Mức độ** ảnh hưởng: THẤP / TRUNG_BÌNH / CAO / KHẨN_CẤP
- Quản lý dự án, công việc liên quan; timeline lịch sử; dashboard; xuất Excel
- **Không** dùng Java Swing (desktop) → **Spring Boot MVC** chạy mọi thiết bị (Windows, Mac, mobile browser)
- **Không** Knowledge Base / AI (để đúng phạm vi thi)
- Mã hóa mật khẩu **MD5**; chỉ **2 vai trò**: ADMIN và USER (+ quản lý user)

### 5 bảng chính

| Bảng | Vai trò |
|------|---------|
| NGUOIDUNG | Đăng nhập, phân quyền ADMIN/USER |
| DUAN | Dự án campus |
| CONGVIEC | Công việc thuộc dự án |
| SUCO | Sự cố + mức độ + tiến độ trạng thái |
| LICHSU | Timeline mỗi lần đổi trạng thái |

---

## 2. Môi trường hỗ trợ

| Thành phần | Hỗ trợ |
|------------|--------|
| JDK | **21 → 27** (pom compile release 21, chạy trên JVM mới hơn) |
| Oracle | XE 18/21 (**XEPDB1**), Oracle Free 23/26ai (**FREEPDB1**), SID XE |
| SQL Developer | Mọi bản hỗ trợ Oracle 12c+ (chạy script F5) |
| OS client | Windows, macOS, Linux, mobile (trình duyệt) |
| IDE | IntelliJ IDEA (khuyến nghị), VS Code + Java |

---

## 3. Cách chạy chi tiết

### Bước A – Oracle (SQL Developer)

```sql
-- SYSTEM trên đúng PDB (XEPDB1 hoặc FREEPDB1)
CREATE USER smartcampus IDENTIFIED BY smartcampus;
GRANT CONNECT, RESOURCE, CREATE VIEW TO smartcampus;
ALTER USER smartcampus QUOTA UNLIMITED ON USERS;
```

Kết nối user `smartcampus` → mở `sql/00_schema_5bang.sql` → **Run Script (F5)**.

### Bước B – Cấu hình JDBC

Sửa `src/main/resources/application.properties` – **một** dòng url:

- XE 21c: `...@localhost:1521/XEPDB1`
- Free / FREEPDB1: `...@localhost:1521/FREEPDB1`

### Bước C – IntelliJ

1. Open thư mục có `pom.xml`
2. SDK = JDK 21 (hoặc 25/26/27)
3. Maven → Reload → **Build → Rebuild Project**
4. Run `com.smartcampus.SmartCampusApplication`

### Bước D – Trình duyệt

http://localhost:8080  

| Email | Mật khẩu | Vai trò |
|-------|----------|---------|
| admin@campus.edu | 123456 | ADMIN |
| user1@campus.edu | 123456 | USER |

---

## 4. Tiến độ chức năng (Exam)

| Module | Trạng thái |
|--------|------------|
| Đăng nhập / Đăng ký MD5 | Hoàn thành |
| Quản lý User (ADMIN) | Hoàn thành |
| CRUD Dự án, Công việc | Hoàn thành |
| Sự cố + mức độ + tiến độ TT | Hoàn thành |
| Timeline LICHSU (transaction) | Hoàn thành |
| Dashboard + quá hạn | Hoàn thành |
| Xuất Excel | Hoàn thành |
| Knowledge Base / AI | **Không làm** (theo phạm vi thi) |

---

## 5. Công nghệ

Spring Boot 3.3 · MVC · Thymeleaf · Bootstrap 5 · JPA · Oracle · MD5 · Apache POI · Maven
