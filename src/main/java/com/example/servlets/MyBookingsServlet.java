package com.example.servlets;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.BookingDAO;
import com.example.model.Booking;
import com.example.model.User;

@WebServlet("/my-bookings")
public class MyBookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            // Проверяем и обновляем просроченные брони
            new BookingDAO().markExpiredBookingsAsCompleted();

            List<Booking> bookings = new BookingDAO().findBookingsByRenterId(user.getId());
            req.setAttribute("bookings", bookings);
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to load bookings: " + e.getMessage());
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
    }
}