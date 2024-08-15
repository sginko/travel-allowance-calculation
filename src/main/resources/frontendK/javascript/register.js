const newuserForm = document.getElementById('register-newuser-form');
newuserForm.addEventListener('submit', submitForm);

async function submitForm(evt) {
    evt.preventDefault();
    console.log('submitted');

    const regFormData = new FormData(newuserForm);
    const dataBody = JSON.stringify(Object.fromEntries(regFormData.entries()));
    
    try {
        const request = await fetch('http://localhost:8080/api/v1/ginko/new-user', {
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'
            },
            method: 'POST',
            body: dataBody
        });
        if (request.ok) {
            alert('Użytkownik został pomyślnie dodany');
            const regContainer = document.getElementById('employee-register-container');
            const userAddedContainer = document.getElementById('user-added-container');
            regContainer.classList.add('hidden');
            userAddedContainer.classList.remove('hidden');

        } else {
            alert('Wystąpił błąd po stronie serwera');
        }


    } catch (error) {
        alert('Wystąpił błąd po stronie użytkownika');
    }
}