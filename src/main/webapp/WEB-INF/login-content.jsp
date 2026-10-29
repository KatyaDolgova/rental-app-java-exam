<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">Вход в аккаунт</h4>
            </div>
            <div class="card-body">
                <form method="POST" action="login" id="loginForm">
                    <div class="mb-3">
                        <label for="nickname" class="form-label">Никнейм</label>
                        <input type="text" class="form-control" id="nickname" name="nickname" required minlength="3">
                        <div class="invalid-feedback">Введите никнейм (минимум 3 символа).</div>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="6">
                        <div class="invalid-feedback">Введите пароль (минимум 6 символов).</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Войти</button>
                </form>
                <div class="mt-3 text-center">
                    <a href="register.jsp">Ещё не зарегистрированы? Зарегистрируйтесь</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        let valid = true;
        const nickname = document.getElementById('nickname');
        const password = document.getElementById('password');

        document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

        if (nickname.value.trim().length < 3) {
            nickname.classList.add('is-invalid');
            nickname.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (password.value.trim().length < 6) {
            password.classList.add('is-invalid');
            password.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!valid) e.preventDefault();
    });
</script>