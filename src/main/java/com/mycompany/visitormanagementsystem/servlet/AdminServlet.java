package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        AdminFilters filters;
        try {
            filters = AdminFilters.from(request);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        try {
            request.setAttribute("status", filters.status());
            request.setAttribute("from", filters.from() == null ? "" : filters.from().toString());
            request.setAttribute("to", filters.to() == null ? "" : filters.to().toString());
            request.setAttribute("search", filters.search());
            request.setAttribute("visitors", visitorRepository.findByFilters(
                    filters.status(), filters.from(), filters.to(), filters.search()));
            request.setAttribute("activeCount", visitorRepository.countByStatus("active"));
            request.setAttribute("pastCount", visitorRepository.countByStatus("past"));
            request.setAttribute("allCount", visitorRepository.countByStatus("all"));
            request.setAttribute("todayCount", visitorRepository.countCheckedInOn(LocalDate.now()));
            request.setAttribute("periodCount", visitorRepository.countInRange(filters.from(), filters.to()));
            request.setAttribute("dailyCounts", dailyCounts(
                    visitorRepository.findDailyCounts(filters.from(), filters.to(), 14)));
            request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Could not load visitor records.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load visitor records.");
        }
    }

    private static List<Map<String, Object>> dailyCounts(List<Object[]> rows) {
        List<Map<String, Object>> days = new ArrayList<>();
        long maximum = 1;
        for (Object[] row : rows) {
            maximum = Math.max(maximum, ((Number) row[1]).longValue());
        }
        DateTimeFormatter labelFormat = DateTimeFormatter.ofPattern("MMM d");
        for (int index = rows.size() - 1; index >= 0; index--) {
            Object[] row = rows.get(index);
            LocalDate date = row[0] instanceof Date sqlDate
                    ? sqlDate.toLocalDate()
                    : (LocalDate) row[0];
            long count = ((Number) row[1]).longValue();
            Map<String, Object> day = new HashMap<>();
            day.put("label", date.format(labelFormat));
            day.put("count", count);
            day.put("barHeight", Math.max(8, Math.round((double) count / maximum * 100)));
            days.add(day);
        }
        return days;
    }
}
