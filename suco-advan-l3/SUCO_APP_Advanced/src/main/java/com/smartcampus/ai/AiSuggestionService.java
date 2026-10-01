package com.smartcampus.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Trợ lý AI – offline stub HOẶC gọi API ngoài (OpenAI / Gemini / Ollama).
 *
 * application.properties:
 *   app.ai.enabled=true
 *   app.ai.api-url=https://api.openai.com/v1/chat/completions
 *   app.ai.api-key=sk-xxxx
 *   app.ai.model=gpt-4o-mini
 */
@Service
public class AiSuggestionService {

    @Value("${app.ai.enabled:false}")
    private boolean enabled;

    @Value("${app.ai.api-url:}")
    private String apiUrl;

    @Value("${app.ai.api-key:}")
    private String apiKey;

    @Value("${app.ai.model:gpt-4o-mini}")
    private String model;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15)).build();

    public boolean isOnlineMode() {
        return enabled && apiUrl != null && !apiUrl.isBlank();
    }

    public String getStatus() {
        if (!enabled) return "Offline (stub cục bộ)";
        if (apiUrl == null || apiUrl.isBlank()) return "Bật nhưng chưa có URL API";
        return "Online → " + apiUrl + " | model=" + model;
    }

    public String suggestSolution(String tieuDe, String moTa, String danhMuc) {
        String prompt = """
                Bạn là kỹ thuật viên CNTT Smart Campus (trường học).
                Sự cố: %s | Danh mục: %s
                Mô tả: %s
                Trả lời tiếng Việt, ngắn:
                1) Nguyên nhân có thể
                2) Các bước kiểm tra / khắc phục
                3) Khi nào cần leo thang
                """.formatted(tieuDe, danhMuc != null ? danhMuc : "Chưa phân loại",
                moTa != null ? moTa : "(không có)");

        if (isOnlineMode()) {
            try { return callExternalApi(prompt); }
            catch (Exception e) {
                return "[API lỗi: " + e.getMessage() + "]\n\n" + offlineSuggest(tieuDe, danhMuc);
            }
        }
        return offlineSuggest(tieuDe, danhMuc);
    }

    public String suggestCategory(String tieuDe, String moTa, List<String> cats) {
        if (cats == null || cats.isEmpty()) return "Khác";
        String prompt = "Chọn ĐÚNG 1 danh mục trong: " + String.join(", ", cats)
                + "\nSự cố: " + tieuDe + "\n" + (moTa != null ? moTa : "")
                + "\nChỉ trả về tên danh mục.";
        if (isOnlineMode()) {
            try {
                String r = callExternalApi(prompt).trim().lines().findFirst().orElse("");
                for (String c : cats) if (r.contains(c) || c.contains(r)) return c;
            } catch (Exception ignored) {}
        }
        return offlineCategory(tieuDe, cats);
    }

    private String callExternalApi(String userPrompt) throws Exception {
        String body = """
                {"model":"%s","messages":[
                  {"role":"system","content":"Trợ lý CNTT Smart Campus, trả lời tiếng Việt ngắn gọn."},
                  {"role":"user","content":%s}
                ],"temperature":0.4,"max_tokens":800}
                """.formatted(model, jsonEscape(userPrompt));

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (apiKey != null && !apiKey.isBlank())
            b.header("Authorization", "Bearer " + apiKey);

        HttpResponse<String> resp = http.send(b.build(), HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400)
            throw new RuntimeException("HTTP " + resp.statusCode());
        return extractContent(resp.body());
    }

    private static String extractContent(String raw) {
        int idx = raw.indexOf("\"content\"");
        if (idx < 0) return raw;
        int start = raw.indexOf('"', idx + 10);
        if (start < 0) return raw;
        start++;
        StringBuilder sb = new StringBuilder();
        boolean esc = false;
        for (int i = start; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (esc) {
                if (c == 'n') sb.append('\n');
                else if (c == 't') sb.append('\t');
                else sb.append(c);
                esc = false;
            } else if (c == '\\') esc = true;
            else if (c == '"') break;
            else sb.append(c);
        }
        return sb.toString();
    }

    private static String jsonEscape(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "") + "\"";
    }

    private String offlineSuggest(String tieuDe, String dm) {
        String d = dm != null ? dm : "";
        StringBuilder sb = new StringBuilder();
        sb.append("【Chế độ offline】\nSự cố: ").append(tieuDe).append("\nDanh mục: ").append(d.isEmpty() ? "Khác" : d).append("\n\n");
        sb.append("1) Nguyên nhân có thể:\n");
        String tl = tieuDe.toLowerCase();
        if (d.contains("Mạng") || tl.contains("wifi") || tl.contains("mạng"))
            sb.append("   - Switch/AP lỗi, DHCP đầy, cáp hỏng, VLAN sai\n");
        else if (d.contains("chiếu") || tl.contains("chiếu"))
            sb.append("   - Bóng đèn, cáp HDMI, nguồn, sai đầu vào\n");
        else if (d.contains("Email") || d.contains("Phần mềm"))
            sb.append("   - License, cache, quyền TK, cập nhật lỗi\n");
        else
            sb.append("   - Thiết bị/phần mềm, nguồn điện, thay đổi cấu hình gần đây\n");
        sb.append("\n2) Kiểm tra: phạm vi ảnh hưởng → restart → log/đèn báo → thử máy/TK khác\n");
        sb.append("3) Leo thang nếu KHẨN CẤP hoặc ảnh hưởng > 1 phòng.\n");
        sb.append("\n→ Bật online: app.ai.enabled=true + api-url + api-key trong application.properties");
        return sb.toString();
    }

    private String offlineCategory(String tieuDe, List<String> cats) {
        String t = tieuDe.toLowerCase();
        for (String c : cats) {
            String cl = c.toLowerCase();
            if ((cl.contains("mạng") || cl.contains("wifi")) && (t.contains("wifi") || t.contains("mạng"))) return c;
            if (cl.contains("chiếu") && t.contains("chiếu")) return c;
            if (cl.contains("in") && t.contains("in")) return c;
            if (cl.contains("email") && t.contains("email")) return c;
            if (cl.contains("lab") && t.contains("lab")) return c;
            if (cl.contains("server") && t.contains("server")) return c;
        }
        return cats.get(cats.size() - 1);
    }
}
