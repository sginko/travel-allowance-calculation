document.getElementById('dbtc-form').addEventListener('submit', sendDbtcRequest);

function validateFormFields(form) {
    const defaultValues = {
        numberOfBreakfasts: 0,
        numberOfLunches: 0,
        numberOfDinners: 0,
        dailyAllowance: 45,
        inputQuantityOfOvernightStayWithoutInvoice: 0,
        inputQuantityOfOvernightStayWithInvoice: 0,
        amountOfTotalOvernightsStayWithInvoice: 0,
        amountOfTotalOvernightsStayWithoutInvoice: 0,
        advancePayment: 0,
        isInvoiceAmountGreaterAllowed: false
    };

    for (const key in defaultValues) {
        if (form[key].value === "" || form[key].value === null) {
            form[key].value = defaultValues[key];
        }
    }

    console.log("Form submitted", {
        pesel: form["pesel"].value,
        numberOfLunches: form["numberOfLunches"].value,
        numberOfBreakfasts: form["numberOfBreakfasts"].value,
        numberOfDinners: form["numberOfDinners"].value,
        inputQuantityOfOvernightStayWithoutInvoice: form["inputQuantityOfOvernightStayWithoutInvoice"].value,
        inputQuantityOfOvernightStayWithInvoice: form["inputQuantityOfOvernightStayWithInvoice"].value,
        amountOfTotalOvernightsStayWithInvoice: form["amountOfTotalOvernightsStayWithInvoice"].value,
        advancePayment: form["advancePayment"].value,
        isInvoiceAmountGreaterAllowed: form["isInvoiceAmountGreaterAllowed"].checked
    });
}

async function sendDbtcRequest(evt) {
    evt.preventDefault();
    const form = evt.target;
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
            inputQuantityOfOvernightStayWithInvoice: parseInt(formData.get("inputQuantityOfOvernightStayWithInvoice")),
            amountOfTotalOvernightsStayWithInvoice: parseFloat(formData.get("amountOfTotalOvernightsStayWithInvoice")),
            isInvoiceAmountGreaterAllowed: form["isInvoiceAmountGreaterAllowed"].checked
        }
    };

    console.log("Sending data", JSON.stringify(jsonObject));

    const jsonData = JSON.stringify(jsonObject);
    try {
        const response = await fetch('http://localhost:8080/api/v1/travels', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            alert(`Błąd: ${response.status}\nMessage: ${response.statusText}`);
            throw new Error(`Błąd: ${response.status}\nMessage: ${response.statusText}`);
        }
        const dataJSON = await response.json();
        localStorage.setItem('resultValues', JSON.stringify(dataJSON));
        window.location.href = 'results.html';

    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas dodawania podróży');
    }
}
