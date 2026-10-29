<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Property" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<%@ page import="java.util.List" %>
<%
    List<Property> properties = (List<Property>) request.getAttribute("properties");
    if (properties == null) {
        properties = new java.util.ArrayList<>();
    }
%>
<div class="row">
    <div class="col-md-12">
        <h3>Мои объявления</h3>
        <%
            if (properties.isEmpty()) {
        %>
        <p>Вы ещё не добавили ни одного объявления.</p>
        <%
            } else {
        %>
        <div class="row">
            <%
                for (Property p : properties) {
            %>
            <div class="col-md-6 mb-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title"><%= HtmlUtils.esc(p.getCity()) %>, <%= HtmlUtils.esc(p.getCountry()) %></h5>
                        <p class="card-text">
                            <strong>Цена за ночь:</strong> $<%= p.getPricePerNight() %><br>
                            <strong>Описание:</strong> <%= HtmlUtils.esc(p.getDescription()) %>
                        </p>
                        <a href="edit-property?propertyId=<%= p.getId() %>" class="btn btn-outline-primary btn-sm">✏️ Редактировать</a>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </div>
        <%
            }
        %>
    </div>
</div>