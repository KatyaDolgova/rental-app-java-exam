<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.BookingWithDetails" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<%@ page import="java.util.List" %>
<%
    List<BookingWithDetails> bookings = (List<BookingWithDetails>) request.getAttribute("bookings");
    if (bookings == null) bookings = new java.util.ArrayList<>();
%>
<div class="row">
    <div class="col-md-12">
        <h3>Брони на моё жильё</h3>
        <% if (bookings.isEmpty()) { %>
            <p>На ваши объявления пока никто не забронировал жильё.</p>
        <% } else { %>
            <table class="table table-striped align-middle">
                <thead>
                    <tr>
                        <th>Жильё</th>
                        <th>Арендатор</th>
                        <th>Даты</th>
                        <th>Статус</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (BookingWithDetails bd : bookings) {
                        var b = bd.getBooking();
                        var p = bd.getProperty();
                        String status = b.getStatus();
                    %>
                    <tr>
                        <td><%= HtmlUtils.esc(p.getCity()) %>, <%= HtmlUtils.esc(p.getCountry()) %></td>
                        <td><%= HtmlUtils.esc(bd.getRenterNickname()) %></td>
                        <td>
                            <%= b.getStartDate() %> — <%= b.getEndDate() %>
                            <% if ("MODIFY_REQUESTED".equals(status) && b.getNewStartDate() != null) { %>
                                <br><small class="text-muted">запрошено: <%= b.getNewStartDate() %> — <%= b.getNewEndDate() %></small>
                            <% } %>
                        </td>
                        <td>
                            <% if ("CANCEL_REQUESTED".equals(status)) { %>
                                <span class="badge bg-warning text-dark">Запрошена отмена</span>
                            <% } else if ("MODIFY_REQUESTED".equals(status)) { %>
                                <span class="badge bg-warning text-dark">Запрошено изменение дат</span>
                            <% } else if ("CANCELLED".equals(status)) { %>
                                <span class="badge bg-secondary">Отменена</span>
                            <% } else if (b.isCompleted()) { %>
                                <span class="badge bg-success">Выполнена</span>
                            <% } else { %>
                                <span class="badge bg-primary">Активна</span>
                            <% } %>
                        </td>
                        <td>
                            <% if ("CANCEL_REQUESTED".equals(status)) { %>
                                <form method="POST" action="respond-booking" class="d-inline">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="action" value="approve_cancel">
                                    <button type="submit" class="btn btn-success btn-sm">Подтвердить отмену</button>
                                </form>
                                <form method="POST" action="respond-booking" class="d-inline">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="action" value="reject_cancel">
                                    <button type="submit" class="btn btn-outline-secondary btn-sm">Отклонить</button>
                                </form>
                            <% } else if ("MODIFY_REQUESTED".equals(status)) { %>
                                <form method="POST" action="respond-booking" class="d-inline">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="action" value="approve_modify">
                                    <button type="submit" class="btn btn-success btn-sm">Подтвердить новые даты</button>
                                </form>
                                <form method="POST" action="respond-booking" class="d-inline">
                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                    <input type="hidden" name="action" value="reject_modify">
                                    <button type="submit" class="btn btn-outline-secondary btn-sm">Отклонить</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>
</div>
