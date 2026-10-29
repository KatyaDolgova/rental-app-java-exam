<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.model.Booking" %>
        <% Booking booking=(Booking) request.getAttribute("booking"); if (booking==null) {
            response.sendRedirect("my-bookings"); return; } %>
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header bg-info text-white">
                            <h4>Оценить владельца (бронирование #<%= booking.getId() %>)</h4>
                        </div>
                        <div class="card-body">
                            <p><strong>Жильё:</strong> #<%= booking.getPropertyId() %>
                            </p>
                            <p><strong>Даты:</strong>
                                <%= booking.getStartDate() %> — <%= booking.getEndDate() %>
                            </p>

                            <form method="POST" action="rate-booking">
                                <input type="hidden" name="bookingId" value="<%= booking.getId() %>">
                                <input type="hidden" name="type" value="owner">
                                <div class="mb-3">
                                    <label class="form-label">Оценка владельцу (1–5)</label>
                                    <select class="form-select" name="rating" required>
                                        <option value="">Выберите оценку</option>
                                        <option value="5">⭐⭐⭐⭐⭐ 5</option>
                                        <option value="4">⭐⭐⭐⭐ 4</option>
                                        <option value="3">⭐⭐⭐ 3</option>
                                        <option value="2">⭐⭐ 2</option>
                                        <option value="1">⭐ 1</option>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Отправить оценку</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>