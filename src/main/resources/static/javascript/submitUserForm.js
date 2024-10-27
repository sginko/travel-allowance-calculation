const form = document.getElementById('register-user-form');
form.addEventListener('submit', submitForm);

async function submitForm(event) {
    event.preventDefault();
    const formData = new FormData(form);
    const jsonData = JSON.stringify(Object.fromEntries(formData.entries()));
    console.log(jsonData);
    // displayMessageBox("user-message-box");
    try {
        const response = await fetch('http://localhost:8080/api/v1/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            const errorData = await response.json();
            displayErrorBox("user-error-box", errorData);
        } else {
            displayMessageBox("user-message-box");
        }

    } catch (error) {
        displayErrorBox("user-error-box", error);
    }
}
