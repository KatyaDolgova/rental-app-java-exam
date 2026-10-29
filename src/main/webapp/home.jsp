<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.model.User" %>

        <% User user=(User) session.getAttribute("currentUser"); if (user==null) { response.sendRedirect("login.jsp");
            return; } // Правильно устанавливаем атрибуты для layout.jsp
            request.setAttribute("title", "Добро пожаловать" ); request.setAttribute("content", "home-content.jsp" ); %>

            <jsp:include page="/WEB-INF/layout.jsp" />