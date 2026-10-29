<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.model.Property" %>
        <%@ page import="com.example.utils.HtmlUtils" %>
        <%@ page import="java.util.List" %>
            <% List<Property> results = (List<Property>) request.getAttribute("results");
                    if (results == null) results = new java.util.ArrayList<>();%>

                        <div class="row">
                            <!-- Левая колонка — фильтры  -->
                            <div class="col-md-4">
                                <div class="card mb-4 shadow-sm">
                                    <div class="card-header bg-info text-white">
                                        <i class="bi bi-funnel"></i> Фильтры поиска
                                    </div>
                                    <div class="card-body">
                                        <form method="GET" action="search">
                                            <div class="mb-3">
                                                <label for="city" class="form-label">Город</label>
                                                <input type="text" class="form-control" id="city" name="city" value="<%= HtmlUtils.esc(request.getParameter("city")) %>" maxlength="100">
                                            </div>
                                            <div class="mb-3">
                                                <label for="country" class="form-label">Страна</label>
                                                <input type="text" class="form-control" id="country" name="country" value="<%= HtmlUtils.esc(request.getParameter("country")) %>" maxlength="100">
                                            </div>
                                            <div class="mb-3">
                                                <label for="minPrice" class="form-label">Мин. цена за ночь ($)</label>
                                                <input type="number" step="0.01" class="form-control" id="minPrice" name="minPrice" value="<%= HtmlUtils.esc(request.getParameter("minPrice")) %>" min="0">
                                            </div>
                                            <div class="mb-3">
                                                <label for="maxPrice" class="form-label">Макс. цена за ночь ($)</label>
                                                <input type="number" step="0.01" class="form-control" id="maxPrice" name="maxPrice" value="<%= HtmlUtils.esc(request.getParameter("maxPrice")) %>" min="0">
                                            </div>
                                            <button type="submit" class="btn btn-primary w-100">
                                                <i class="bi bi-search"></i> Найти
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Правая колонка — результаты -->
                            <div class="col-md-8">
                                <h3 class="mb-4">Найдено объявлений: <span class="badge bg-primary">
                                        <%= results.size() %>
                                    </span></h3>

                                <% if (results.isEmpty()) { %>
                                    <div class="alert alert-info text-center p-4">
                                        Объявлений не найдено. Попробуйте изменить фильтры.
                                    </div>
                                    <% } else { %>
                                        <div class="row row-cols-1 row-cols-md-2 g-4">
                                            <% for (Property p : results) { %>
                                                <div class="col">
                                                    <div class="card h-100 shadow-sm">
                                                        <div class="card-body">
                                                            <h5 class="card-title">
                                                                <%= HtmlUtils.esc(p.getCity()) %>, <%= HtmlUtils.esc(p.getCountry()) %>
                                                            </h5>
                                                            <p class="card-text">
                                                                <strong>$<%= p.getPricePerNight() %></strong> за
                                                                ночь<br>
                                                                <% if (p.getOwnerRating() !=null) { %>
                                                                    Рейтинг владельца: <span class="text-warning">★ <%=
                                                                            String.format("%.1f", p.getOwnerRating()) %>
                                                                            </span>
                                                                    <% } %>
                                                            </p>
                                                            <p class="card-text text-truncate">
                                                                <%= HtmlUtils.esc(p.getDescription()) %>
                                                            </p>
                                                            <a href="book-property?propertyId=<%= p.getId() %>" class="btn btn-primary w-100">
                                                                Забронировать
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                                <% } %>
                                        </div>
                                        <% } %>
                            </div>
                        </div>