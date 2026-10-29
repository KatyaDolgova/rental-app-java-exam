<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header bg-success text-white">
                <h4 class="mb-0">Регистрация нового пользователя</h4>
            </div>
            <div class="card-body">
                <form method="POST" action="register" id="registrationForm">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Полное имя</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" required minlength="2" maxlength="50">
                        <div class="invalid-feedback">Имя должно быть от 2 до 50 символов.</div>
                    </div>
                    <div class="mb-3">
                        <label for="city" class="form-label">Город</label>
                        <input type="text" class="form-control" id="city" name="city" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label for="country" class="form-label">Страна</label>
                        <input type="text" class="form-control" id="country" name="country" maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label for="nickname" class="form-label">Никнейм</label>
                        <input type="text" class="form-control" id="nickname" name="nickname" required minlength="3" maxlength="20" pattern="[a-zA-Z0-9_]+">
                        <div class="invalid-feedback">Никнейм должен быть от 3 до 20 символов и содержать только буквы, цифры и _.</div>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                        <div class="invalid-feedback">Введите корректный email.</div>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="6" maxlength="128">
                        <div class="invalid-feedback">Пароль должен быть не менее 6 символов.</div>
                    </div>
                    <div class="mb-3">
                        <label for="birthDate" class="form-label">Дата рождения</label>
                        <input type="date" class="form-control" id="birthDate" name="birthDate" required>
                        <div class="invalid-feedback">Дата рождения обязательна и вы должны быть старше 13 лет.</div>
                    </div>
                    <button type="submit" class="btn btn-success w-100">Зарегистрироваться</button>
                </form>
                <div class="mt-3 text-center">
                    <a href="login.jsp">Уже есть аккаунт? Войти</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.getElementById('registrationForm').addEventListener('submit', function(e) {
    let valid = true;
    const fullName = document.getElementById('fullName');
    const birthDate = document.getElementById('birthDate');
    const nickname = document.getElementById('nickname');

    document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

    // Проверка возраста
    if (birthDate.value) {
        const birth = new Date(birthDate.value);
        const today = new Date();
        let age = today.getFullYear() - birth.getFullYear();
        const monthDiff = today.getMonth() - birth.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
            age--;
        }
        if (age < 13) {
            birthDate.classList.add('is-invalid');
            birthDate.nextElementSibling.style.display = 'block';
            valid = false;
        }
    }

    if (!valid) {
        e.preventDefault();
    }
});
</script>