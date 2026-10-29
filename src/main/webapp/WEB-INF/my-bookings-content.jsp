<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.model.Booking" %>
        <%@ page import="java.util.List" %>
            <% List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                    if (bookings == null) bookings = new java.util.ArrayList<>();
                        %>
                        <div class="row">
                            <div class="col-md-12">
                                <h3>Мои бронирования</h3>
                                <% if (bookings.isEmpty()) { %>
                                    <p>У вас нет бронирований.</p>
                                    <% } else { %>
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Жильё</th>
                                                    <th>Дата начала</th>
                                                    <th>Дата окончания</th>
                                                    <th>Статус</th>
                                                    <th>Действия</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (Booking b : bookings) {
                                                    String status = b.getStatus();
                                                %>
                                                    <tr>
                                                        <td>
                                                            <%= b.getId() %>
                                                        </td>
                                                        <td>Жильё #<%= b.getPropertyId() %>
                                                        </td>
                                                        <td>
                                                            <%= b.getStartDate() %>
                                                        </td>
                                                        <td>
                                                            <%= b.getEndDate() %>
                                                        </td>
                                                        <td>
                                                            <% if ("CANCEL_REQUESTED".equals(status)) { %>
                                                                <span class="badge bg-warning text-dark">Запрошена отмена</span>
                                                            <% } else if ("MODIFY_REQUESTED".equals(status)) { %>
                                                                <span class="badge bg-warning text-dark">Запрошено изменение дат</span>
                                                            <% } else if ("CANCELLED".equals(status)) { %>
                                                                <span class="badge bg-secondary">Отменена</span>
                                                            <% } else if (b.isCompleted()) { %>
                                                                <span class="badge bg-success">Выполнен</span>
                                                                <% } else { %>
                                                                    <span class="badge bg-primary">Активен</span>
                                                                    <% } %>
                                                        </td>
                                                        <td>
                                                            <% if (b.isCompleted() && b.getRatingGivenByOwner()==null) {
                                                                %>
                                                                <a href="rate-booking?bookingId=<%= b.getId() %>" class="btn btn-outline-info btn-sm">
                                                                    ⭐ Оценить владельца
                                                                </a>
                                                            <% } %>
                                                            <% if (!b.isCompleted() && "ACTIVE".equals(status)) { %>
                                                                <form method="POST" action="request-cancel-booking" class="d-inline" onsubmit="return confirm('Запросить отмену брони?');">
                                                                    <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                                                    <button type="submit" class="btn btn-outline-danger btn-sm">Отменить</button>
                                                                </form>
                                                                <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="collapse" data-bs-target="#modifyForm<%= b.getId() %>">
                                                                    Изменить даты
                                                                </button>
                                                                <div class="collapse mt-2" id="modifyForm<%= b.getId() %>">
                                                                    <form method="POST" action="request-modify-booking" class="row g-2 align-items-end">
                                                                        <input type="hidden" name="bookingId" value="<%= b.getId() %>">
                                                                        <div class="col-auto">
                                                                            <label class="form-label small">Новая дата начала</label>
                                                                            <input type="date" class="form-control form-control-sm" name="newStartDate" required>
                                                                        </div>
                                                                        <div class="col-auto">
                                                                            <label class="form-label small">Новая дата окончания</label>
                                                                            <input type="date" class="form-control form-control-sm" name="newEndDate" required>
                                                                        </div>
                                                                        <div class="col-auto">
                                                                            <button type="submit" class="btn btn-primary btn-sm">Отправить запрос</button>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            <% } %>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    <% } %>
                            </div>
                        </div>
