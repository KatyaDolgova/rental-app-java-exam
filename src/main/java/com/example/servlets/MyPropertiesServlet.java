package com.example.servlets;

import com.example.dao.PropertyDAO;
import com.example.model.Property;
import com.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-properties")
public class MyPropertiesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            // Предположим, у вас есть метод в PropertyDAO, который возвращает все жильё пользователя
            List<Property> properties = new PropertyDAO().findPropertiesByOwnerId(user.getId());
            req.setAttribute("properties", properties);
            req.getRequestDispatcher("my-properties.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to load properties: " + e.getMessage());
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
    }
}