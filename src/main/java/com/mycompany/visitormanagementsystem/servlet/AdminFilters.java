package com.mycompany.visitormanagementsystem.servlet;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

record AdminFilters(String status, LocalDate from, LocalDate to, String search) {
    static AdminFilters from(HttpServletRequest request) {
        String status = request.getParameter("status");
        if (status == null || status.isBlank()) {
            status = "active";
        }
        if (!status.equals("active") && !status.equals("past") && !status.equals("all")) {
            throw new IllegalArgumentException("Choose Active, Past, or All visitors.");
        }

        LocalDate from = parseDate(request.getParameter("from"), "Start date");
        LocalDate to = parseDate(request.getParameter("to"), "End date");
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must be on or before the end date.");
        }

        String search = request.getParameter("search");
        search = search == null ? "" : search.trim();
        if (search.length() > 100) {
            throw new IllegalArgumentException("Search text must be 100 characters or fewer.");
        }
        return new AdminFilters(status, from, to, search);
    }

    private static LocalDate parseDate(String value, String label) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(label + " must be a valid date.");
        }
    }
}
