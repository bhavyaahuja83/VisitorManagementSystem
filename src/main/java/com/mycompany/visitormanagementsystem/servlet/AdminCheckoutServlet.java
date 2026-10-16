package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/checkout")
public class AdminCheckoutServlet extends HttpServlet {
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        long visitorId;
        try {
            visitorId = Long.parseLong(request.getParameter("visitorId"));
            if (visitorId < 1) {
                throw new NumberFormatException("Visitor id must be positive.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid visitor id.");
            return;
        }

        String status = request.getParameter("status");
        if (!"all".equals(status) && !"active".equals(status) && !"past".equals(status)) {
            status = "active";
        }
        try {
            if (!visitorRepository.checkOut(visitorId)) {
                response.sendError(HttpServletResponse.SC_CONFLICT,
                        "The visitor was already checked out or could not be found.");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin?status=" + status + "&checkedOut=1");
        } catch (RuntimeException e) {
            getServletContext().log("Could not check out visitor " + visitorId + ".", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to check out visitor.");
        }
    }
}
