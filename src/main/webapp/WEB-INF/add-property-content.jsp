<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-success text-white">
                <h4 class="mb-0">Добавить новое жильё</h4>
            </div>
            <div class="card-body">
                <form method="POST" action="add-property" id="addPropertyForm">
                    <div class="mb-3">
                        <label for="city" class="form-label">Город</label>
                        <input type="text" class="form-control" id="city" name="city" required minlength="1" maxlength="100">
                        <div class="invalid-feedback">Город обязателен (1-100 символов).</div>
                    </div>
                    <div class="mb-3">
                        <label for="country" class="form-label">Страна</label>
                        <input type="text" class="form-control" id="country" name="country" required minlength="1" maxlength="100">
                        <div class="invalid-feedback">Страна обязательна (1-100 символов).</div>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Описание</label>
                        <textarea class="form-control" id="description" name="description" rows="4" required minlength="5" maxlength="1000"></textarea>
                        <div class="invalid-feedback">Описание должно быть от 5 до 1000 символов.</div>
                    </div>
                    <div class="mb-3">
                        <label for="price" class="form-label">Цена за ночь ($)</label>
                        <input type="number" step="0.01" class="form-control" id="price" name="price" required min="0.01" max="999999.99">
                        <div class="invalid-feedback">Введите положительную цену (0.01 - 999999.99).</div>
                    </div>
                    <button type="submit" class="btn btn-success w-100">Добавить</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('addPropertyForm').addEventListener('submit', function(e) {
        let valid = true;
        const city = document.getElementById('city');
        const country = document.getElementById('country');
        const description = document.getElementById('description');
        const price = document.getElementById('price');

        document.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

        if (city.value.trim().length < 1 || city.value.trim().length > 100) {
            city.classList.add('is-invalid');
            city.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (country.value.trim().length < 1 || country.value.trim().length > 100) {
            country.classList.add('is-invalid');
            country.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (description.value.trim().length < 5 || description.value.trim().length > 1000) {
            description.classList.add('is-invalid');
            description.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (parseFloat(price.value) <= 0 || parseFloat(price.value) > 999999.99) {
            price.classList.add('is-invalid');
            price.nextElementSibling.style.display = 'block';
            valid = false;
        }

        if (!valid) e.preventDefault();
    });
</script>