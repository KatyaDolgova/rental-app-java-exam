<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Property" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<% Property prop=(Property) request.getAttribute("property"); if (prop==null) {
response.sendRedirect("my-properties"); return; } %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-warning text-white">
                <h4>Редактировать объявление: <%= HtmlUtils.esc(prop.getCity()) %>, <%= HtmlUtils.esc(prop.getCountry()) %></h4>
            </div>
            <div class="card-body">
                <form method="POST" action="edit-property" id="editPropertyForm">
                    <input type="hidden" name="propertyId" value="<%= prop.getId() %>">
                    <div class="mb-3">
                        <label class="form-label">Город</label>
                        <input type="text" class="form-control" name="city" value="<%= HtmlUtils.esc(prop.getCity()) %>" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Страна</label>
                        <input type="text" class="form-control" name="country" value="<%= HtmlUtils.esc(prop.getCountry()) %>" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Описание</label>
                        <textarea class="form-control" name="description" rows="4" maxlength="1000"><%= HtmlUtils.esc(prop.getDescription()) %></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Цена за ночь ($)</label>
                        <input type="number" step="0.01" class="form-control" name="price" value="<%= prop.getPricePerNight() %>" min="0.01" max="999999.99">
                        <div class="invalid-feedback">Введите положительную цену (0.01 - 999999.99).</div>
                    </div>
                    <button type="submit" class="btn btn-warning w-100">Сохранить изменения</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('editPropertyForm').addEventListener('submit', function(e) {
        let valid = true;
        const price = document.querySelector('input[name="price"]');

        document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

        if (parseFloat(price.value) <= 0 || parseFloat(price.value) > 999999.99) {
            price.classList.add('is-invalid');
            price.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!valid) e.preventDefault();
    });
</script>