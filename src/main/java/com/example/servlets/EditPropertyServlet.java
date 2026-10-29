package com.example.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.dao.PropertyDAO;
import com.example.model.Property;
import com.example.model.User;

@WebServlet("/edit-property")
public class EditPropertyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        if (propertyIdParam == null || propertyIdParam.trim().isEmpty()) {
            req.setAttribute("error", "propertyId is required");
            req.getRequestDispatcher("my-properties.jsp").forward(req, resp);
            return;
        }

        try {
            Long propertyId = Long.parseLong(propertyIdParam);
            PropertyDAO dao = new PropertyDAO();
            Property prop = dao.findById(propertyId);

            if (prop == null || !prop.getOwnerId().equals(user.getId())) {
                req.setAttribute("error", "Вы не владелец этого объявления.");
                req.getRequestDispatcher("my-properties.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("title", "Редактировать объявление");
            req.setAttribute("content", "edit-property-content.jsp");
            req.setAttribute("property", prop); // ← важно!
            req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID объявления.");
            req.getRequestDispatcher("my-properties.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка загрузки формы редактирования: " + e.getMessage());
            req.getRequestDispatcher("my-properties.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        if (propertyIdParam == null) {
            resp.sendRedirect("my-properties.jsp");
            return;
        }

        try {
            Long propertyId = Long.parseLong(propertyIdParam);
            PropertyDAO dao = new PropertyDAO();
            Property prop = dao.findById(propertyId);

            if (prop == null || !prop.getOwnerId().equals(user.getId())) {
                resp.sendRedirect("my-properties.jsp");
                return;
            }

            String city = req.getParameter("city");
            String country = req.getParameter("country");
            String description = req.getParameter("description");
            String priceParam = req.getParameter("price");

            if (city != null && !city.trim().isEmpty()) prop.setCity(city);
            if (country != null && !country.trim().isEmpty()) prop.setCountry(country);
            if (description != null && !description.trim().isEmpty()) prop.setDescription(description);
            if (priceParam != null && !priceParam.trim().isEmpty()) {
                prop.setPricePerNight(new java.math.BigDecimal(priceParam));
            }

            dao.update(prop);
            req.setAttribute("message", "✅ Объявление обновлено!");
            resp.sendRedirect("my-properties.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка сохранения: " + e.getMessage());
            req.getRequestDispatcher("edit-property.jsp?propertyId=" + propertyIdParam).forward(req, resp);
        }
    }
}