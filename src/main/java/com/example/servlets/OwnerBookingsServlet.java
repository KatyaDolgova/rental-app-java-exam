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
import com.example.model.BookingWithDetails;
import com.example.model.User;

@WebServlet("/owner-bookings")
public class OwnerBookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            List<BookingWithDetails> bookings = new BookingDAO().findBookingsForOwner(user.getId());
            req.setAttribute("title", "Брони на моё жильё");
            req.setAttribute("content", "owner-bookings-content.jsp");
            req.setAttribute("bookings", bookings);
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Не удалось загрузить брони: " + e.getMessage());
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
    }
}
