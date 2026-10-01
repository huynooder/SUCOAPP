# SUCO_APP Advanced – Smart Campus (mở rộng)

## So với Exam

| Tính năng | Exam | Advanced |
|-----------|------|----------|
| 5 bảng chính | ✓ | ✓ |
| **12 Danh mục** campus | ✗ | ✓ |
| **Trợ lý AI** (menu riêng) | ✗ | ✓ |
| **Cây quan hệ** sự cố | ✗ | ✓ |
| UI hiện đại | Cơ bản | Sidebar + card + gradient |
| Gọi API ngoài (OpenAI/Ollama) | ✗ | ✓ (tùy chọn) |
| REST API | ✗ | `/api/incidents` |
| Bình luận | ✗ | Bảng BINHLUAN |

## Menu mới
- **Danh mục** – 12 nhóm: Mạng, PC, Máy chiếu, Phần mềm, Email, Máy in, Server, An ninh, Điện/PCCC, Website, Lab, Khác
- **Cây quan hệ** – Dự án → Danh mục → Sự cố + biểu đồ
- **Trợ lý AI** – gợi ý xử lý & phân loại (offline / online)

## Cài đặt

### SQL Developer
1. `sql/00_schema_5bang.sql`
2. `sql/03_schema_advanced.sql`  ← 12 danh mục

### IntelliJ
- Open project → JDK 21+ → Run `SmartCampusApplication`
- http://localhost:8080  
  admin@campus.edu / 123456

### Bật AI online (mạng ngoài)
Trong `application.properties`:
```properties
app.ai.enabled=true
app.ai.api-url=https://api.openai.com/v1/chat/completions
app.ai.api-key=sk-...
app.ai.model=gpt-4o-mini
```
Hoặc Ollama local: `http://localhost:11434/v1/chat/completions`

## Lỗi VAI_TRO?
Xem `HUONG_DAN_SUA_LOI.md` – cần Rebuild sau khi cập nhật.
