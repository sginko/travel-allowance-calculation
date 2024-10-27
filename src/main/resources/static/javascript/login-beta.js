const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', submitForm);

async function submitForm(evt) {
    evt.preventDefault();
    console.log('login');

    const loginFormData = new FormData(loginForm);
    const dataBody = new URLSearchParams(loginFormData).toString(); // Преобразование в URL-кодированную строку

    const token = document.querySelector('input[name="${_csrf.parameterName}"]').value; // Получение CSRF токена

    try {
        const request = await fetch('http://localhost:8080/login', {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': token,
                'Accept': 'application/json'
            },
            method: 'POST',
            body: dataBody // Отправка URL-кодированных данных
        });

        if (request.ok) {
            alert('Użytkownik został zalogowany');

            window.location.href = 'http://localhost:8080/pages/main.html';
        } else {
            alert('Wystąpił błąd po stronie сервера');
        }
    } catch (error) {
        alert('Wystąpił błąd po stronie użytkownika');
    }
}
