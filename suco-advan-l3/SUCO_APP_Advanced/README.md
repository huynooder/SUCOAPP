# SUCO_APP Advanced – Smart Campus (mở rộng)

## 1. Giới thiệu

Kế thừa **Exam** (web MVC, 5 bảng, MD5, ADMIN/USER, mức độ, tiến độ) và mở rộng:

- **12 danh mục** sự cố campus (Mạng, PC, Máy chiếu, Lab…)
- **Trợ lý AI** offline (mặc định) / online (OpenAI, Ollama)
- **Cây quan hệ** Dự án → Danh mục → Sự cố + biểu đồ
- **UI hiện đại** (sidebar, card, badge)
- REST API stub, bảng BINHLUAN
- Tham khảo tiến độ/SQL quản trị từ bộ IMS_TEST (Oracle Free FREEPDB1)

**Không** dùng Swing. Chạy web trên Windows / Mac / mobile.

## 2. Môi trường

Giống Exam: JDK 21–27, Oracle XEPDB1 hoặc FREEPDB1, SQL Developer mọi bản gần đây.

## 3. Cách chạy

1. SQL: `00_schema_5bang.sql` rồi `03_schema_advanced.sql` (user smartcampus trên đúng PDB)
2. `application.properties` → url XEPDB1 hoặc FREEPDB1
3. IntelliJ Rebuild → Run SmartCampusApplication
4. http://localhost:8080 — admin@campus.edu / 123456

### Menu Advanced

Dashboard · Dự án · Công việc · Sự cố · **Danh mục** · **Cây quan hệ** · **Trợ lý AI** · User · Excel

### AI

Mặc định offline. Online: xem README_ADVANCED.md / cấu hình `app.ai.*`.

## 4. Tiến độ Advanced

| Module | Trạng thái |
|--------|------------|
| Toàn bộ Exam | Done |
| 12 danh mục + CRUD | Done |
| AI offline/online | Done |
| Cây quan hệ + Chart.js | Done |
| UI hiện đại | Done |
| REST /api/incidents | Stub Done |
