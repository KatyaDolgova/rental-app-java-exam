package com.example.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.PasswordUtils;

@WebServlet("/edit-profile")
public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        req.setAttribute("user", user);
        req.getRequestDispatcher("edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String fullName = req.getParameter("fullName");
        String city = req.getParameter("city");
        String country = req.getParameter("country");
        String email = req.getParameter("email");
        String newPassword = req.getParameter("password");

        // --- Валидация ---
        StringBuilder errorMessage = new StringBuilder();
        boolean isValid = true;

        if (fullName == null || fullName.trim().length() < 2 || fullName.trim().length() > 50) {
            errorMessage.append("И 2 до 50 символов. ");
            isValid = false;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errorMessage.append("Некорректный email. ");
            isValid = false;
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (newPassword.length() < 6 || newPassword.length() > 128) {
                errorMessage.append("Пароль должен быть от 6 до 128 символов. ");
                isValid = false;
            }
        }

        if (!isValid) {
            req.setAttribute("error", errorMessage.toString());
            req.getRequestDispatcher("edit-profile.jsp").forward(req, resp);
            return;
        }
        

        user.setFullName(fullName.trim());
        user.setCity(city != null ? city.trim() : null);
        user.setCountry(country != null ? country.trim() : null);
        user.setEmail(email.trim());
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(PasswordUtils.hash(newPassword));
        }

        try {
            new UserDAO().update(user);
            session.setAttribute("currentUser", user);
            req.setAttribute("message", "✅ Профиль обновлён!");
            resp.sendRedirect("home.jsp");
        } catch (SQLException e) {
            req.setAttribute("error", "Ошибка обновления профиля: " + e.getMessage());
            req.getRequestDispatcher("edit-profile.jsp").forward(req, resp);
        }
    }
}