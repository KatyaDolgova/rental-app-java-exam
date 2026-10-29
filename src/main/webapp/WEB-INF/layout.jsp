<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.utils.HtmlUtils" %>
    <!DOCTYPE html>
    <html lang="ru">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            <%= request.getAttribute("title") !=null ? HtmlUtils.esc(request.getAttribute("title")) : "Онлайн-аренда жилья" %>
        </title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                padding-top: 60px;
            }

            .card {
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
        </style>
    </head>

    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
            <div class="container">
                <a class="navbar-brand" href="home.jsp">🏠 Онлайн-аренда жилья</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item"><a class="nav-link" href="home.jsp">Главная</a></li>
                        <li class="nav-item"><a class="nav-link" href="search">Поиск жилья</a></li>
                        <% Object currentUser=session.getAttribute("currentUser"); if (currentUser !=null) { %>
                            <li class="nav-item"><a class="nav-link" href="my-properties">Мои объявления</a></li>
                            <li class="nav-item"><a class="nav-link" href="my-bookings">Мои бронирования</a></li>
                            <li class="nav-item"><a class="nav-link" href="owner-bookings">Брони на моё жильё</a></li>
                            <li class="nav-item"><a class="nav-link" href="edit-profile">Редактировать профиль</a></li>
                            <li class="nav-item"><a class="nav-link" href="add-property.jsp">Добавить жильё</a></li>
                            <li class="nav-item">
                                <form method="POST" action="logout" class="d-inline">
                                    <button type="submit" class="btn btn-outline-light btn-sm">Выход</button>
                                </form>
                            </li>
                            <% } else { %>
                                <li class="nav-item"><a class="nav-link" href="register.jsp">Регистрация</a></li>
                                <li class="nav-item"><a class="nav-link" href="login.jsp">Вход</a></li>
                                <% } %>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container mt-4">
            <% if (request.getAttribute("message") !=null) { %>
                <div class="alert alert-success alert-dismissible fade show">
                    <%= HtmlUtils.esc(request.getAttribute("message")) %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% } %>
                    <% if (request.getAttribute("error") !=null) { %>
                        <div class="alert alert-danger alert-dismissible fade show">
                            <%= HtmlUtils.esc(request.getAttribute("error")) %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    <% } %>

                <jsp:include page='<%= (String) request.getAttribute("content") %>' />
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>