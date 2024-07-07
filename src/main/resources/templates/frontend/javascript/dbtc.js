const dbtcForm = document.getElementById("dbtc-form");
dbtcForm.addEventListener('submit', sendDbtcRequest);

function validateFormFields(form) {


    if (form["numberOfBreakfasts"].value === "") {
        form["numberOfBreakfasts"].value = 0;
    }
    if (form["numberOfLunches"].value === "") {
        form["numberOfLunches"].value = 0;
    }
    if (form["numberOfDinners"].value === "") {
        form["numberOfDinners"].value = 0;
    }
    if (form["dailyAllowance"].value === "") {
        form["dailyAllowance"].value = 45;
    }
    if (form["inputQuantityOfOvernightStayWithoutInvoice"].value === "") {
        form["inputQuantityOfOvernightStayWithoutInvoice"].value = 0;
    }
    if (form["inputQuantityOfOvernightStayWithInvoice"].value === "") {
        form["inputQuantityOfOvernightStayWithInvoice"].value = 0;
    }
    if (form["amountOfTotalOvernightsStayWithInvoice"].value === "") {
        form["amountOfTotalOvernightsStayWithInvoice"].value = 0;
    }
    if (form["advancePayment"].value === "") {
        form["advancePayment"].value = 0;
    }

    console.log("from submited", form["pesel"].value, form["numberOfLunches"].value,
    form["numberOfBreakfasts"].value, form["numberOfDinners"].value),
    form["inputQuantityOfOvernightStayWithoutInvoice"].value,
    form["inputQuantityOfOvernightStayWithInvoice"].value, form["amountOfTotalOvernightsStayWithInvoice"].value,
    form["advancePayment"].value;
}


async function sendDbtcRequest(evt) {
    evt.preventDefault();
    validateFormFields(dbtcForm);
    const dbtcFormData = new FormData(dbtcForm);
    const dbtcJSONData = JSON.stringify(Object.fromEntries(dbtcFormData.entries()));
    console.log(dbtcJSONData);
    try {
        const response = await fetch('http://localhost:8080/api/v1/travels', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: dbtcJSONData
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Обработка успешного ответа
        console.log('Podróż została pomyślnie dodana');
        alert('Podróż została pomyślnie dodana');

    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas dodawania podróży');
    }
}