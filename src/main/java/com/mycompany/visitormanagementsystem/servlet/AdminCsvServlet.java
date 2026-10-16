package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.model.Visitor;
import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/admin/export")
public class AdminCsvServlet extends HttpServlet {
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        AdminFilters filters;
        try {
            filters = AdminFilters.from(request);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        try {
            List<Visitor> visitors = visitorRepository.findByFilters(
                    filters.status(), filters.from(), filters.to(), filters.search());
            response.setContentType("text/csv");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename=\"visitor-report.csv\"");
            response.setHeader("Cache-Control", "private, no-store");

            var writer = response.getWriter();
            writer.write('\ufeff');
            writer.println("Visitor ID,Visitor name,Mobile number,Person to visit,Purpose,Check-in,Check-out,Status");
            for (Visitor visitor : visitors) {
                writeRow(writer, List.of(
                        "VM-" + visitor.getId(),
                        visitor.getVisitorName(),
                        visitor.getMobileNumber(),
                        visitor.getPersonToVisit(),
                        visitor.getPurpose(),
                        visitor.getCheckInAtDisplay(),
                        visitor.getCheckOutAtDisplay(),
                        visitor.getCheckOutAt() == null ? "Active" : "Past"
                ));
            }
        } catch (RuntimeException e) {
            getServletContext().log("Could not export visitor records.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to export visitor records.");
        }
    }

    private static void writeRow(java.io.PrintWriter writer, List<String> values) {
        writer.println(values.stream().map(AdminCsvServlet::csvValue).reduce((a, b) -> a + "," + b).orElse(""));
    }

    private static String csvValue(String value) {
        String safeValue = value;
        if (!safeValue.isEmpty()
                && "=+-@\t\r\n".indexOf(safeValue.charAt(0)) >= 0) {
            safeValue = "'" + safeValue;
        }
        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }
}
