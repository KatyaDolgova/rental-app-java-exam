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
import com.example.dao.UserDAO;
import com.example.model.Booking;
import com.example.model.Property;
import com.example.model.User;

@WebServlet("/rate-booking")
public class RateBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String bookingIdParam = req.getParameter("bookingId");
        if (bookingIdParam == null || bookingIdParam.trim().isEmpty()) {
            req.setAttribute("error", "bookingId is required");
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
            return;
        }

        try {
            Long bookingId = Long.parseLong(bookingIdParam);
            BookingDAO bookingDao = new BookingDAO();
            Booking booking = bookingDao.findById(bookingId);

            if (booking == null || !booking.isCompleted()) {
                req.setAttribute("error", "Бронь не найдена или ещё не завершена.");
                req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
                return;
            }

            Property prop = new PropertyDAO().findById(booking.getPropertyId());
            if (prop == null) {
                req.setAttribute("error", "Жильё не найдено.");
                req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
                return;
            }

            if (!booking.getRenterId().equals(currentUser.getId()) ||
                booking.getRatingGivenByOwner() != null) {
                req.setAttribute("error", "Вы не можете оценить этого владельца.");
                req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("title", "Оценить владельца");
            req.setAttribute("content", "rate-booking-content.jsp");
            req.setAttribute("booking", booking);
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID брони.");
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка загрузки формы оценки: " + e.getMessage());
            req.getRequestDispatcher("my-bookings.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Long bookingId = Long.parseLong(req.getParameter("bookingId"));
        String type = req.getParameter("type");
        int rating = Integer.parseInt(req.getParameter("rating"));

        try {
            BookingDAO bookingDao = new BookingDAO();
            Booking booking = bookingDao.findById(bookingId);
            if (booking == null || !booking.isCompleted()) {
                req.setAttribute("error", "Booking not found or not completed.");
                req.getRequestDispatcher("rate-booking.jsp?bookingId=" + bookingId).forward(req, resp);
                return;
            }

            Property property = new PropertyDAO().findById(booking.getPropertyId());
            Long ownerId = property.getOwnerId();

            if ("owner".equals(type) && currentUser.getId().equals(booking.getRenterId()) && booking.getRatingGivenByOwner() == null) {
                booking.setRatingGivenByOwner(rating);
                updateUserRating(ownerId, rating);
            } else if ("renter".equals(type) && currentUser.getId().equals(ownerId) && booking.getRatingGivenByRenter() == null) {
                booking.setRatingGivenByRenter(rating);
                updateUserRating(booking.getRenterId(), rating);
            } else {
                req.setAttribute("error", "Вам не разрешается оценивать этого пользователя.");
                req.getRequestDispatcher("rate-booking.jsp?bookingId=" + bookingId).forward(req, resp);
                return;
            }

            String column = "owner".equals(type) ? "rating_given_by_owner"
                           : "renter".equals(type) ? "rating_given_by_renter"
                           : null;
            if (column == null) {
                throw new IllegalArgumentException("Unknown rating type: " + type);
            }
            String sql = "UPDATE bookings SET " + column + " = ? WHERE id = ?";
            try (var conn = com.example.utils.DBConnection.getConnection();
                var stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, rating);
                stmt.setLong(2, bookingId);
                stmt.executeUpdate();
            }

            req.setAttribute("message", "✅ Оценка отправлена!");
            resp.sendRedirect("home.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка сохранения оценки: " + e.getMessage());
            req.getRequestDispatcher("rate-booking.jsp?bookingId=" + req.getParameter("bookingId")).forward(req, resp);
        }
    }

    private void updateUserRating(Long userId, int newRating) throws Exception {
        UserDAO userDao = new UserDAO();
        User user = userDao.findByUserId(userId);
        if (user == null) throw new Exception("User not found");

        double newRatingSum = user.getRatingSum() + newRating;
        int newRatingCount = user.getRatingCount() + 1;
        double newAverageRating = newRatingSum / newRatingCount;

        String sql = "UPDATE users SET rating_sum = ?, rating_count = ?, rating = ? WHERE id = ?";
        try (var conn = com.example.utils.DBConnection.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newRatingSum);
            stmt.setInt(2, newRatingCount);
            stmt.setDouble(3, newAverageRating);
            stmt.setLong(4, userId);
            stmt.executeUpdate();
        }

        if (newAverageRating < 2.0) {
            userDao.blockUser(userId);
        }
    }
}