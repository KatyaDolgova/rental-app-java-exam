package com.example.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.PropertyDAO;
import com.example.model.Property;

@WebServlet("/search")
public class SearchPropertiesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String city = req.getParameter("city");
        String country = req.getParameter("country");
        BigDecimal minPrice = parseBigDecimal(req.getParameter("minPrice"));
        BigDecimal maxPrice = parseBigDecimal(req.getParameter("maxPrice"));

        PropertyDAO dao = new PropertyDAO();
        List<Property> results;

        try {
            results = dao.findPropertiesWithOwnerRating(city, country, minPrice, maxPrice);
        } catch (SQLException e) {
            req.setAttribute("error", "Ошибка базы: " + e.getMessage());
            req.getRequestDispatcher("search.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("results", results);
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}