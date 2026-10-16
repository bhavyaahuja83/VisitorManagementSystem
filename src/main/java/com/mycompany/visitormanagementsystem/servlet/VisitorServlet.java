package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.model.Visitor;
import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/visitor")
public class VisitorServlet extends HttpServlet {
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/visitor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String visitorName = value(request, "visitorName");
        String mobileNumber = value(request, "mobileNumber");
        String personToVisit = value(request, "personToVisit");
        String purpose = value(request, "purpose");

        request.setAttribute("visitorName", visitorName);
        request.setAttribute("mobileNumber", mobileNumber);
        request.setAttribute("personToVisit", personToVisit);
        request.setAttribute("purpose", purpose);

        if (visitorName.isBlank() || mobileNumber.isBlank()
                || personToVisit.isBlank() || purpose.isBlank()) {
            request.setAttribute("error", "Please complete all fields.");
            request.getRequestDispatcher("/WEB-INF/views/visitor.jsp").forward(request, response);
            return;
        }
        if (!mobileNumber.matches("[+0-9() .-]{7,25}")
                || mobileNumber.replaceAll("\\D", "").length() < 7
                || mobileNumber.replaceAll("\\D", "").length() > 15) {
            request.setAttribute("error", "Enter a valid mobile number (7 to 15 digits).");
            request.getRequestDispatcher("/WEB-INF/views/visitor.jsp").forward(request, response);
            return;
        }
        if (visitorName.length() > 120 || personToVisit.length() > 120 || purpose.length() > 500) {
            request.setAttribute("error", "One or more fields exceed the allowed length.");
            request.getRequestDispatcher("/WEB-INF/views/visitor.jsp").forward(request, response);
            return;
        }

        Visitor visitor = new Visitor();
        visitor.setVisitorName(visitorName);
        visitor.setMobileNumber(mobileNumber);
        visitor.setPersonToVisit(personToVisit);
        visitor.setPurpose(purpose);
        try {
            visitorRepository.save(visitor);
        } catch (RuntimeException e) {
            getServletContext().log("Could not save visitor entry.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to save your visit. Please try again later.");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/visitor?submitted=1");
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}
