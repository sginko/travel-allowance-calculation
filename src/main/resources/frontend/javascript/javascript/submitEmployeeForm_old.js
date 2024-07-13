document.getElementById('register-employee-form').addEventListener('submit', submitForm);

async function submitForm(event) {
    event.preventDefault();
    const form = document.getElementById('register-employee-form');
    const formData = new FormData(form);
    const jsonData = JSON.stringify(Object.fromEntries(formData.entries()));

    try {
        const response = await fetch('http://localhost:8080/api/v1/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        console.log('Pracownik został pomyślnie dodany');
        alert('Pracownik został pomyślnie dodany');
    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas dodawania pracownika');
    }
}

