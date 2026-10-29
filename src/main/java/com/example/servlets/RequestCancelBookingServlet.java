package com.example.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.BookingDAO;
import com.example.model.Booking;
import com.example.model.User;

@WebServlet("/request-cancel-booking")
public class RequestCancelBookingServlet extends HttpServlet {

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
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
            return;
        }

        try {
            BookingDAO dao = new BookingDAO();
            Booking booking = dao.findById(bookingId);

            if (booking == null || !booking.getRenterId().equals(user.getId())) {
                req.setAttribute("error", "У вас нет разрешения отменять это бронирование.");
                req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
                return;
            }

            if (booking.isCompleted()) {
                req.setAttribute("error", "Невозможно отменить завершенное бронирование.");
                req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
                return;
            }

            dao.updateStatus(bookingId, "CANCEL_REQUESTED");

            req.setAttribute("message", "Требуется отмена бронирования. Ждем подтверждения от владельца.");
            resp.sendRedirect("my-bookings.jsp");

        } catch (SQLException e) {
            req.setAttribute("error", "Не удалось запросить отмену бронирования: " + e.getMessage());
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
        }
    }
}