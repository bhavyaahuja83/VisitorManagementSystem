package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.config.AppConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isAdmin(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (matches(username, AppConfig.adminUsername())
                && matches(password, AppConfig.adminPassword())) {
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            request.getSession(true).setAttribute("isAdmin", Boolean.TRUE);
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        request.setAttribute("error", "Incorrect username or password.");
        request.getRequestDispatcher("/WEB-INF/views/admin-login.jsp").forward(request, response);
    }

    private static boolean isAdmin(HttpSession session) {
        return session != null && Boolean.TRUE.equals(session.getAttribute("isAdmin"));
    }

    private static boolean matches(String supplied, String expected) {
        if (supplied == null) {
            return false;
        }
        return MessageDigest.isEqual(
                supplied.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8)
        );
    }
}
