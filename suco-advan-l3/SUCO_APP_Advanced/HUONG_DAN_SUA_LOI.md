# HƯỚNG DẪN SỬA LỖI ORA-00904 VAI_TRO + CHẠY ADVANCED

## Lỗi bạn gặp
```
ORA-00904: "ND1_0"."VAI_TRO": invalid identifier
```
Hibernate đang hỏi cột `VAI_TRO` (có gạch dưới) trong khi Oracle chỉ có `VAITRO`.

## Cách 1 – Dùng zip mới (khuyên dùng)

1. Xóa / đổi tên thư mục cũ `D:\BT\SUCO_APP_Advanced`
2. Giải nén file zip **mới** vào `D:\BT\`
3. IntelliJ → Open project mới
4. **Maven → Reload project** (chuột phải pom.xml → Maven → Reload)
5. **Build → Rebuild Project**
6. Chạy lại `SmartCampusApplication`

## Cách 2 – Sửa nhanh project đang mở

### Bước A: application.properties
Mở `src/main/resources/application.properties`, **thêm cuối file**:

```properties
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
```

### Bước B: Entity NguoiDung
Mở `entity/NguoiDung.java`, đổi mọi `@Column` thành UPPERCASE:

```java
@Column(name = "MAND")
@Column(name = "HOTEN", ...)
@Column(name = "EMAIL", ...)
@Column(name = "MATKHAU", ...)
@Column(name = "VAITRO", ...)   // ← quan trọng, không viết VaiTro hay VAI_TRO
@Column(name = "TRANGTHAI", ...)
@Column(name = "NGAYTAO")
```

Làm tương tự các entity khác (SuCo, DuAn, CongViec, LichSu) nếu còn lỗi cột.

### Bước C: Rebuild
- IntelliJ: **Build → Rebuild Project**
- Hoặc terminal: `mvn clean spring-boot:run`
- Xóa thư mục `target/` nếu cần

## SQL Developer – chạy schema

1. Chạy `sql/00_schema_5bang.sql` (5 bảng chính) nếu chưa
2. (Advanced) Chạy thêm `sql/03_schema_advanced.sql` → thêm DANHMUC + BINHLUAN

## Tài khoản test
| Email | MK | Vai trò |
|-------|-----|---------|
| admin@campus.edu | 123456 | ADMIN |
| user1@campus.edu | 123456 | USER |

## Advanced có thêm gì?
- Bảng **DANHMUC** (Mạng, Phần cứng, Phần mềm, Điện, Khác)
- Bảng **BINHLUAN** (comment trên sự cố)
- Cột MaDM trên SUCO
- Stub AI + REST API
- Mức độ: THAP / TRUNG_BINH / CAO / KHAN_CAP (đã có trong schema)
