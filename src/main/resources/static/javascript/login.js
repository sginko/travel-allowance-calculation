const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', submitForm);

async function submitForm(evt) {
    evt.preventDefault();
    console.log('login');

    const loginFormData = new URLSearchParams();
    loginFormData.append('username', loginForm.username.value);
    loginFormData.append('password', loginForm.password.value);

    try {
        const request = await fetch('http://localhost:8080/login', {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            method: 'POST',
            credentials: 'include',
            body: loginFormData.toString()
        });
        if (request.ok) {
            alert('Użytkownik został zalogowany');
            window.location.href = 'http://localhost:8080/pages/main.html';
        } else {
            alert('Wystąpił błąd po stronie serwera');
        }
    } catch (error) {
        alert('Wystąpił błąd po stronie użytkownika');
    }
}
