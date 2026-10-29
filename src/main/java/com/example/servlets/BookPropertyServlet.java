package com.example.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.BookingDAO;
import com.example.dao.PropertyDAO;
import com.example.model.Booking;
import com.example.model.Property;
import com.example.model.User;

@WebServlet("/book-property")
public class BookPropertyServlet extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        resp.sendRedirect("login.jsp");
        return;
    }

    String propertyIdParam = req.getParameter("propertyId");
    if (propertyIdParam == null || propertyIdParam.trim().isEmpty()) {
        req.setAttribute("error", "propertyId is required");
        req.getRequestDispatcher("search.jsp").forward(req, resp);
        return;
    }

    try {
        Long propertyId = Long.parseLong(propertyIdParam);
        PropertyDAO dao = new PropertyDAO();
        Property prop = dao.findById(propertyId);

        if (prop == null) {
            req.setAttribute("error", "Жильё не найдено.");
            req.getRequestDispatcher("search.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("title", "Забронировать жильё");
        req.setAttribute("content", "book-property-content.jsp");
        req.setAttribute("property", prop);
        req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

    } catch (NumberFormatException e) {
        req.setAttribute("error", "Неверный формат ID жилья.");
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    } catch (Exception e) {
        e.printStackTrace();
        req.setAttribute("error", "Ошибка загрузки формы бронирования: " + e.getMessage());
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    }
}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        // --- Валидация ---
        StringBuilder errorMessage = new StringBuilder();
        boolean isValid = true;

        // 1. ID жилья
        Long propertyId = null;
        try {
            propertyId = Long.parseLong(propertyIdParam);
        } catch (NumberFormatException e) {
            errorMessage.append("Некорректный ID жилья. ");
            isValid = false;
        }

        // 2. Даты
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            } else {
                errorMessage.append("Дата начала обязательна. ");
                isValid = false;
            }
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            } else {
                errorMessage.append("Дата окончания обязательна. ");
                isValid = false;
            }
        } catch (Exception e) {
            errorMessage.append("Некорректный формат даты. Используйте YYYY-MM-DD. ");
            isValid = false;
        }

        //3. Логика дат
        if (isValid && startDate != null && endDate != null) {
            // if (startDate.isBefore(LocalDate.now())) {
            //     errorMessage.append("Дата начала не может быть в прошлом. ");
            //     isValid = false;
            // }
            if (endDate.isBefore(startDate)) {
                errorMessage.append("Дата окончания не может быть раньше даты начала. ");
                isValid = false;
            }
        }

        if (!isValid) {
            req.setAttribute("error", errorMessage.toString());
            req.setAttribute("propertyId", propertyIdParam); 
            req.setAttribute("title", "Забронировать жильё");
            req.setAttribute("content", "book-property-content.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
            return;
        }

        // --- Создание бронирования ---
        Booking booking = new Booking();
        booking.setPropertyId(propertyId);
        booking.setRenterId(user.getId());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);

        try {
            new BookingDAO().save(booking);
            req.setAttribute("message", "✅ Бронирование создано! Ожидайте подтверждения.");
            resp.sendRedirect("home.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка создания бронирования: " + e.getMessage());
            req.setAttribute("propertyId", propertyIdParam);
            req.setAttribute("title", "Забронировать жильё");
            req.setAttribute("content", "book-property-content.jsp");
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        }
    }
}