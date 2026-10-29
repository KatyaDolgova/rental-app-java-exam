package com.example.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.utils.PasswordUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String nickname = req.getParameter("nickname");
    String password = req.getParameter("password");

    // --- Валидация ---
    if (nickname == null || nickname.trim().isEmpty()) {
        req.setAttribute("error", "Введите никнейм.");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return;
    }
    if (password == null || password.trim().isEmpty()) {
        req.setAttribute("error", "Введите пароль.");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        return;
    }
    

    try {
        User user = new UserDAO().findByNickname(nickname.trim());
        if (user == null) {
            req.setAttribute("error", "Неверный никнейм или пароль.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if (!PasswordUtils.verify(password, user.getPassword())) {
            req.setAttribute("error", "Неверный никнейм или пароль.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if (user.isBlocked()) {
            req.setAttribute("error", "Ваш аккаунт заблокирован.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("currentUser", user);
        resp.sendRedirect("home.jsp");
    } catch (Exception e) {
        req.setAttribute("error", "Ошибка входа. Попробуйте позже.");
        e.printStackTrace();
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
}