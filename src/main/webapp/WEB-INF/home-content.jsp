<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<%
    User user = (User) session.getAttribute("currentUser");
%>
<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-body">
                <h2 class="card-title">Здравствуйте, <%= HtmlUtils.esc(user.getNickname()) %>!</h2>
                <p class="card-text">
                    Ваш рейтинг: <strong><%= String.format("%.1f", user.getRating()) %> ⭐</strong>
                    <% if (user.isBlocked()) { %>
                        <span class="badge bg-danger">Заблокирован</span>
                    <% } else { %>
                        <span class="badge bg-success">Активен</span>
                    <% } %>
                </p>
                <hr>
                <p><strong>Полное имя:</strong> <%= HtmlUtils.esc(user.getFullName()) %></p>
                <p><strong>Email:</strong> <%= HtmlUtils.esc(user.getEmail()) %></p>
                <p><strong>Город:</strong> <%= user.getCity() != null ? HtmlUtils.esc(user.getCity()) : "Не указан" %></p>
                <p><strong>Страна:</strong> <%= user.getCountry() != null ? HtmlUtils.esc(user.getCountry()) : "Не указана" %></p>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                Быстрые действия
            </div>
            <div class="card-body">
                <a href="add-property" class="btn btn-primary w-100 mb-2">➕ Добавить жильё</a>
                <a href="search" class="btn btn-outline-primary w-100 mb-2">🔍 Поиск жилья</a>
                <a href="my-properties" class="btn btn-outline-secondary w-100 mb-2">🏠 Мои объявления</a>
                <a href="my-bookings" class="btn btn-outline-secondary w-100">📅 Мои бронирования</a>
            </div>
        </div>

        <div class="card">
            <div class="card-header bg-info text-white">
                Помощь
            </div>
            <div class="card-body">
                <p>Вы — арендодатель? Вы можете сдавать жильё.</p>
                <p>Вы — арендатор? Ищите подходящее жильё по цене и месту.</p>
            </div>
        </div>
    </div>
</div>