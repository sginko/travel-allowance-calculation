const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', submitForm);

async function submitForm(evt) {
    evt.preventDefault();
    console.log('login');

    const loginFormData = new FormData(loginForm);
    const dataBody = JSON.stringify(Object.fromEntries(loginFormData.entries()));
    
    try {
        const request = await fetch('http://localhost:8080/api/v1/ginko/apps/', {
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'
            },
            method: 'POST',
            body: dataBody
        });
        if (request.ok) {
            alert('Użytkownik został zalogowany');

        } else {
            alert('Wystąpił błąd po stronie serwera');
        }


    } catch (error) {
        alert('Wystąpił błąd po stronie użytkownika');
    }
}