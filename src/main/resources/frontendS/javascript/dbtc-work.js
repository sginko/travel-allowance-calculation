document.addEventListener('DOMContentLoaded', () => {
    const dbtcForm = document.getElementById("dbtc-form");
    dbtcForm.addEventListener('submit', sendDbtcRequest);
});

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
    if (!form["isInvoiceAmountGreaterAllowed"].checked) {
        form["isInvoiceAmountGreaterAllowed"].value = false;
    }
    if (form["inputtedDaysNumberForUndocumentedLocalTransportCost"].value === "") {
        form["inputtedDaysNumberForUndocumentedLocalTransportCost"].value = 0;
    }
    if (form["documentedLocalTransportCost"].value === "") {
        form["documentedLocalTransportCost"].value = 0;
    }
    if (form["meansOfTransport"].value === "") {
        form["meansOfTransport"].value = 0;
    }
    if (form["costOfTravelByPublicTransport"].value === "") {
        form["costOfTravelByPublicTransport"].value = 0;
    }
    if (form["kilometersByCarEngineUpTo900cc"].value === "") {
        form["kilometersByCarEngineUpTo900cc"].value = 0;
    }
    if (form["kilometersByCarEngineAbove900cc"].value === "") {
        form["kilometersByCarEngineAbove900cc"].value = 0;
    }
    if (form["kilometersByMotorcycle"].value === "") {
        form["kilometersByMotorcycle"].value = 0;
    }
    if (form["kilometersByMoped"].value === "") {
        form["kilometersByMoped"].value = 0;
    }
    if (form["otherExpenses"].value === "") {
        form["otherExpenses"].value = 0;
    }


    console.log("Form submitted", {
        pesel: form["pesel"].value,
        numberOfLunches: form["numberOfLunches"].value,
        numberOfBreakfasts: form["numberOfBreakfasts"].value,
        numberOfDinners: form["numberOfDinners"].value,
        inputQuantityOfOvernightStayWithoutInvoice: form["inputQuantityOfOvernightStayWithoutInvoice"].value,
        inputQuantityOfOvernightStayWithInvoice: form["inputQuantityOfOvernightStayWithInvoice"].value,
        amountOfTotalOvernightsStayWithInvoice: form["amountOfTotalOvernightsStayWithInvoice"].value,
        advancePayment: form["advancePayment"].value
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
        otherExpenses: parseFloat(formData.get("otherExpenses")),
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
        },
        transportCostDto: {
            inputtedDaysNumberForUndocumentedLocalTransportCost: parseInt(formData.get("inputtedDaysNumberForUndocumentedLocalTransportCost")),
            documentedLocalTransportCost: parseFloat(formData.get("documentedLocalTransportCost")),
            meansOfTransport: formData.get("meansOfTransport"),
            costOfTravelByPublicTransport: parseInt(formData.get("costOfTravelByPublicTransport")),
            kilometersByCarEngineUpTo900cc: parseInt(formData.get("kilometersByCarEngineUpTo900cc")),
            kilometersByCarEngineAbove900cc: parseInt(formData.get("kilometersByCarEngineAbove900cc")),
            kilometersByMotorcycle: parseInt(formData.get("kilometersByMotorcycle")),
            kilometersByMoped: parseInt(formData.get("kilometersByMoped")),
        }
    };

    const jsonData = JSON.stringify(jsonObject);
    console.log("Sending data", jsonData);

   // try {
        const response = await fetch('http://localhost:8080/api/v1/travels', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: jsonData
        });

        if (!response.ok) {
            const errorData = await response.json();
            alert(`Error: ${response.status}\nMessage: ${errorData.message}`);
            throw new Error(`Error: ${response.status}\nMessage: ${errorData.message}`);
        }

        const dataJSON = await response.json();
        localStorage.setItem('resultValues', JSON.stringify(dataJSON));
        window.location.href = 'results.html';

//    } catch (error) {
//        console.error('Error:', error);
//        alert('Error');
//    }
}
