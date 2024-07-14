document.getElementById('register-user-form').addEventListener('submit', submitForm);

async function submitForm(event) {
    event.preventDefault();
    clearErrors();
    const form = document.getElementById('register-user-form');
    const formData = new FormData(form);
    const jsonData = JSON.stringify(Object.fromEntries(formData.entries()));

    try {
        const response = await fetch('http://localhost:8080/api/v1/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            const errorText = await response.text();
            displayErrors(errorText);
            throw new Error('Network response was not ok');
        }

        console.log('Pracownik został pomyślnie dodany');
        alert('User został pomyślnie dodany');
    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas dodawania usera');
    }
}

function displayErrors(errors) {
    const errorList = errors.split('\n');
    errorList.forEach(error => {
        const [field, message] = error.split(': ');
        const inputField = document.querySelector(`[name=${field}]`);
        if (inputField) {
            const errorMessageElement = document.createElement('div');
            errorMessageElement.classList.add('error-message');
            errorMessageElement.innerText = message;
            inputField.parentNode.appendChild(errorMessageElement);
        }
    });
}

function clearErrors() {
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(errorMessage => errorMessage.remove());
}
