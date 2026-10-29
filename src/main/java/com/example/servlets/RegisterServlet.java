package com.example.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.PasswordUtils;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String city = req.getParameter("city");
        String country = req.getParameter("country");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String birthDateStr = req.getParameter("birthDate");

        // --- Валидация ---
        StringBuilder errorMessage = new StringBuilder();
        boolean isValid = true;

        // Проверка полного имени
        if (fullName == null || fullName.trim().length() < 2 || fullName.trim().length() > 50) {
            errorMessage.append("Имя должно быть от 2 до 50 символов. ");
            isValid = false;
        }

        // Проверка никнейма
        if (nickname == null || nickname.trim().length() < 2 || nickname.trim().length() > 20) {
            errorMessage.append("Никнейм должен быть от 2 до 20 символов. ");
            isValid = false;
        } else if (!nickname.matches("^[a-zA-Z0-9_]+$")) {
            errorMessage.append("Никнейм может содержать только буквы, цифры и символ подчеркивания. ");
            isValid = false;
        }

        // Проверка пароля
        if (password == null || password.length() < 6 || password.length() > 128) {
            errorMessage.append("Пароль должен быть от 6 до 128 символов. ");
            isValid = false;
        }

        // Проверка email
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errorMessage.append("Некорректный email. ");
            isValid = false;
        }

        // Проверка даты рождения
        LocalDate birthDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                birthDate = LocalDate.parse(birthDateStr);
                LocalDate now = LocalDate.now();
                if (birthDate.isAfter(now.minusYears(13))) {
                    errorMessage.append("Пользователь должен быть старше 13 лет. ");
                    isValid = false;
                }
            } catch (Exception e) {
                errorMessage.append("Некорректная дата рождения. ");
                isValid = false;
            }
        } else {
            errorMessage.append("Дата рождения обязательна. ");
            isValid = false;
        }

        if (!isValid) {
            req.setAttribute("error", errorMessage.toString());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setCity(city != null ? city.trim() : null);
        user.setCountry(country != null ? country.trim() : null);
        user.setNickname(nickname.trim());
        user.setPassword(PasswordUtils.hash(password));
        user.setEmail(email.trim());
        user.setBirthDate(birthDate);

        try {
            new UserDAO().save(user);
            req.setAttribute("message", "Регистрация успешна!");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Ошибка регистрации: " + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка сервера. Попробуйте позже.");
            e.printStackTrace();
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}