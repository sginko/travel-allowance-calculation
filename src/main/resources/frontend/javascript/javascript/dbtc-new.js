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

    console.log("Form validated", {
        pesel: form["pesel"].value,
        fromCity: form["fromCity"].value,
        toCity: form["toCity"].value,
        startDate: form["startDate"].value,
        startTime: form["startTime"].value,
        endDate: form["endDate"].value,
        endTime: form["endTime"].value,
        advancePayment: form["advancePayment"].value,
        numberOfBreakfasts: form["numberOfBreakfasts"].value,
        numberOfLunches: form["numberOfLunches"].value,
        numberOfDinners: form["numberOfDinners"].value,
        inputQuantityOfOvernightStayWithoutInvoice: form["inputQuantityOfOvernightStayWithoutInvoice"].value,
        inputQuantityOfOvernightStayWithInvoice: form["inputQuantityOfOvernightStayWithInvoice"].value,
        amountOfTotalOvernightsStayWithInvoice: form["amountOfTotalOvernightsStayWithInvoice"].value,
        isInvoiceAmountGreaterAllowed: form["isInvoiceAmountGreaterAllowed"].checked
    });
}

async function sendDbtcRequest(evt) {
    evt.preventDefault();
    const form = evt.target; // Получаем ссылку на форму
    validateFormFields(form);

    const formData = new FormData(form);
    const jsonObject = {
        pesel: formData.get("pesel"),
        fromCity: formData.get("fromCity"),
        toCity: formData.get("toCity"),
        startDate: formData.get("startDate"),
        startTime: formData.get("startTime"),
        endDate: formData.get("endDate"),
        endTime: formData.get("endTime"),
        advancePayment: parseFloat(formData.get("advancePayment")),
        dietDto: {
            dailyAllowance: parseFloat(formData.get("dailyAllowance")),
            numberOfBreakfasts: parseInt(formData.get("numberOfBreakfasts")),
            numberOfLunches: parseInt(formData.get("numberOfLunches")),
            numberOfDinners: parseInt(formData.get("numberOfDinners"))
        },
        overnightStayDto: {
            inputQuantityOfOvernightStayWithoutInvoice: parseInt(formData.get("inputQuantityOfOvernightStayWithoutInvoice")),
            amountOfTotalOvernightsStayWithoutInvoice: parseFloat(formData.get("amountOfTotalOvernightsStayWithoutInvoice")),
            inputQuantityOfOvernightStayWithInvoice: parseInt(formData.get("inputQuantityOfOvernightStayWithInvoice")),
            amountOfTotalOvernightsStayWithInvoice: parseFloat(formData.get("amountOfTotalOvernightsStayWithInvoice")),
            isInvoiceAmountGreaterAllowed: form["isInvoiceAmountGreaterAllowed"].checked
        }
    };

    const jsonData = JSON.stringify(jsonObject);
    console.log("Sending data", jsonData);

    try {
        const response = await fetch('http://localhost:8080/api/v1/travels', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Server error:', errorData);
            throw new Error('Network response was not ok');
        }

        console.log('Podróż została pomyślnie dodana');
        alert('Podróż została pomyślnie dodана');
    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas dodawания podróży');
    }
}
