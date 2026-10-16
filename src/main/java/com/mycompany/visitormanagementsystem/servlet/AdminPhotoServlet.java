package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/photo")
public class AdminPhotoServlet extends HttpServlet {
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            long id = Long.parseLong(request.getParameter("id"));
            var visitor = visitorRepository.findById(id);
            if (visitor.isEmpty() || visitor.get().getVisitorPhoto() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "private, no-store");
            response.getOutputStream().write(visitor.get().getVisitorPhoto());
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid visitor id.");
        } catch (RuntimeException e) {
            getServletContext().log("Could not load visitor photo.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load visitor photo.");
        }
    }
}
