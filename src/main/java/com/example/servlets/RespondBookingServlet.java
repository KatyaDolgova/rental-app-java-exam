package com.example.servlets;

import java.io.IOException;

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

@WebServlet("/respond-booking")
public class RespondBookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Long bookingId;
        try {
            bookingId = Long.parseLong(req.getParameter("bookingId"));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID бронирования.");
            req.getRequestDispatcher("owner-bookings.jsp").forward(req, resp);
            return;
        }
        String action = req.getParameter("action");

        try {
            BookingDAO bookingDao = new BookingDAO();
            Booking booking = bookingDao.findById(bookingId);
            if (booking == null) {
                req.setAttribute("error", "Бронирование не найдено.");
                req.getRequestDispatcher("owner-bookings.jsp").forward(req, resp);
                return;
            }

            Property property = new PropertyDAO().findById(booking.getPropertyId());
            if (property == null || !property.getOwnerId().equals(user.getId())) {
                req.setAttribute("error", "У вас нет прав отвечать на это бронирование.");
                req.getRequestDispatcher("owner-bookings.jsp").forward(req, resp);
                return;
            }

            switch (action == null ? "" : action) {
                case "approve_cancel":
                    if ("CANCEL_REQUESTED".equals(booking.getStatus())) {
                        bookingDao.cancelBooking(bookingId);
                        req.setAttribute("message", "Отмена брони подтверждена.");
                    }
                    break;
                case "reject_cancel":
                    if ("CANCEL_REQUESTED".equals(booking.getStatus())) {
                        bookingDao.revertToActive(bookingId);
                        req.setAttribute("message", "Запрос на отмену отклонён, бронь снова активна.");
                    }
                    break;
                case "approve_modify":
                    if ("MODIFY_REQUESTED".equals(booking.getStatus())) {
                        bookingDao.applyModification(bookingId);
                        req.setAttribute("message", "Новые даты подтверждены.");
                    }
                    break;
                case "reject_modify":
                    if ("MODIFY_REQUESTED".equals(booking.getStatus())) {
                        bookingDao.revertToActive(bookingId);
                        req.setAttribute("message", "Запрос на изменение дат отклонён.");
                    }
                    break;
                default:
                    req.setAttribute("error", "Неизвестное действие.");
            }

            resp.sendRedirect("owner-bookings");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка обработки запроса: " + e.getMessage());
            req.getRequestDispatcher("owner-bookings.jsp").forward(req, resp);
        }
    }
}
