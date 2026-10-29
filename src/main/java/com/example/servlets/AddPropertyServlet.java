package com.example.servlets;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.PropertyDAO;
import com.example.model.Property;
import com.example.model.User;

@WebServlet("/add-property")
public class AddPropertyServlet extends HttpServlet {
    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        resp.sendRedirect("login.jsp");
        return;
    }

    req.setAttribute("title", "Добавить жильё");
    req.setAttribute("content", "add-property-content.jsp");
    req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        String city = req.getParameter("city");
        String country = req.getParameter("country");
        String description = req.getParameter("description");
        String priceParam = req.getParameter("price");

        // --- Валидация ---
        StringBuilder errorMessage = new StringBuilder();
        boolean isValid = true;

        if (city == null || city.trim().length() < 1 || city.trim().length() > 100) {
            errorMessage.append("Город должен быть от 1 до 100 символов. ");
            isValid = false;
        }

        if (country == null || country.trim().length() < 1 || country.trim().length() > 100) {
            errorMessage.append("Страна должна быть от 1 до 100 символов. ");
            isValid = false;
        }

        if (description == null || description.trim().length() < 5 || description.trim().length() > 1000) {
            errorMessage.append("Описание должно быть от 5 до 1000 символов. ");
            isValid = false;
        }

        BigDecimal price = null;
        try {
            price = new BigDecimal(priceParam);
            if (price.compareTo(BigDecimal.ZERO) <= 0 || price.scale() > 2) {
                errorMessage.append("Цена должна быть положительным числом с максимум 2 знаками после запятой. ");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Некорректная цена. ");
            isValid = false;
        }

        if (!isValid) {
            req.setAttribute("error", errorMessage.toString());
            req.getRequestDispatcher("add-property.jsp").forward(req, resp);
            return;
        }
        

        Property prop = new Property();
        prop.setOwnerId(user.getId());
        prop.setCity(city.trim());
        prop.setCountry(country.trim());
        prop.setDescription(description.trim());
        prop.setPricePerNight(price);

        try {
            new PropertyDAO().save(prop);
            req.setAttribute("message", "✅ Жильё добавлено успешно!");
            resp.sendRedirect("my-properties.jsp");
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка добавления: " + e.getMessage());
            e.printStackTrace();
            req.getRequestDispatcher("add-property.jsp").forward(req, resp);
        }
    }
}