package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        try {
            request.setAttribute("visitors", visitorRepository.findAllNewestFirst());
            request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Could not load visitor records.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load visitor records.");
        }
    }
}
