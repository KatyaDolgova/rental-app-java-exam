<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Property" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<% Property prop=(Property) request.getAttribute("property"); if (prop==null) {
response.sendRedirect("search"); return; } %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4>Забронировать: <%= HtmlUtils.esc(prop.getCity()) %>, <%= HtmlUtils.esc(prop.getCountry()) %></h4>
            </div>
            <div class="card-body">
                <p><strong>Цена за ночь:</strong> $<%= prop.getPricePerNight() %></p>
                <p><strong>Описание:</strong> <%= HtmlUtils.esc(prop.getDescription()) %></p>
                <form method="POST" action="book-property" id="bookPropertyForm">
                    <input type="hidden" name="propertyId" value="<%= prop.getId() %>">
                    <div class="mb-3">
                        <label class="form-label">Дата начала</label>
                        <input type="date" class="form-control" name="startDate" required id="startDate">
                        <!-- <div class="invalid-feedback">Дата начала обязательна и не может быть в прошлом.</div> -->
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Дата окончания</label>
                        <input type="date" class="form-control" name="endDate" required id="endDate">
                        <div class="invalid-feedback">Дата окончания обязательна и должна быть после даты начала.</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Забронировать</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('bookPropertyForm').addEventListener('submit', function(e) {
        let valid = true;
        const startInput = document.getElementById('startDate');
        const endInput = document.getElementById('endDate');

        document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

        const start = new Date(startInput.value);
        const end = new Date(endInput.value);
        const now = new Date();

        // if (start < now.setHours(0,0,0,0)) {
        //     startInput.classList.add('is-invalid');
        //     startInput.nextElementSibling.textContent = 'Дата начала не может быть в прошлом.';
        //     startInput.nextElementSibling.style.display = 'block';
        //     valid = false;
        // }

        if (end < start) {
            endInput.classList.add('is-invalid');
            endInput.nextElementSibling.textContent = 'Дата окончания должна быть после даты начала.';
            endInput.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!valid) e.preventDefault();
    });
</script>