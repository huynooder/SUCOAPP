package com.smartcampus.config;

import com.smartcampus.entity.NguoiDung;
import com.smartcampus.util.SessionHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**", "/error");
    }

    static class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession(false);
            NguoiDung user = session != null ? SessionHelper.getUser(session) : null;
            if (user == null) {
                response.sendRedirect("/login");
                return false;
            }
            // Admin-only paths
            String path = request.getRequestURI();
            if (path.startsWith("/users") && !user.isAdmin()) {
                response.sendRedirect("/incidents?error=forbidden");
                return false;
            }
            return true;
        }
    }
}
