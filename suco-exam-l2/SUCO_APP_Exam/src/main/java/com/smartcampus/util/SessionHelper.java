package com.smartcampus.util;

import com.smartcampus.entity.NguoiDung;
import jakarta.servlet.http.HttpSession;

public final class SessionHelper {
    public static final String SESSION_USER = "currentUser";

    private SessionHelper() {}

    public static void setUser(HttpSession session, NguoiDung user) {
        session.setAttribute(SESSION_USER, user);
    }

    public static NguoiDung getUser(HttpSession session) {
        Object obj = session.getAttribute(SESSION_USER);
        return obj instanceof NguoiDung u ? u : null;
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(SESSION_USER);
        session.invalidate();
    }

    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }

    public static boolean isAdmin(HttpSession session) {
        NguoiDung u = getUser(session);
        return u != null && "ADMIN".equals(u.getVaiTro());
    }
}
