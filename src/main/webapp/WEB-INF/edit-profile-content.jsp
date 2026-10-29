<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<%@ page import="com.example.utils.HtmlUtils" %>
<%
User user = (User) request.getAttribute("user");
if (user == null) {
response.sendRedirect("home");
return;
}
%>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-warning text-white">
                <h4 class="mb-0">Редактировать профиль</h4>
            </div>
            <div class="card-body">
                <form method="POST" action="edit-profile" id="editProfileForm">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Полное имя</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" value="<%= HtmlUtils.esc(user.getFullName()) %>" required minlength="2" maxlength="50">
                        <div class="invalid-feedback">Имя должно быть от 2 до 50 символов.</div>
                    </div>
                    <div class="mb-3">
                        <label for="city" class="form-label">Город</label>
                        <input type="text" class="form-control" id="city" name="city" value="<%= HtmlUtils.esc(user.getCity()) %>" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label for="country" class="form-label">Страна</label>
                        <input type="text" class="form-control" id="country" name="country" value="<%= HtmlUtils.esc(user.getCountry()) %>" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="<%= HtmlUtils.esc(user.getEmail()) %>" required>
                        <div class="invalid-feedback">Введите корректный email.</div>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Новый пароль (оставьте пустым, чтобы не менять)</label>
                        <input type="password" class="form-control" id="password" name="password" minlength="6" maxlength="128">
                        <div class="invalid-feedback">Пароль должен быть не менее 6 символов.</div>
                    </div>
                    <button type="submit" class="btn btn-warning w-100">Сохранить изменения</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('editProfileForm').addEventListener('submit', function(e) {
        let valid = true;
        const fullName = document.getElementById('fullName');
        const email = document.getElementById('email');
        const password = document.getElementById('password');

        document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

        if (fullName.value.trim().length < 2 || fullName.value.trim().length > 50) {
            fullName.classList.add('is-invalid');
            fullName.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!email.value.trim() || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            email.classList.add('is-invalid');
            email.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (password.value.trim() && (password.value.length < 6 || password.value.length > 128)) {
            password.classList.add('is-invalid');
            password.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!valid) e.preventDefault();
    });
</script>