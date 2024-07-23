const form = document.getElementById("dbtc-form");
form.addEventListener('submit', sendDbtcRequest);

async function sendDbtcRequest(evt) {
    evt.preventDefault();

    const formData = new FormData(form);
    const jsonObject = {
        pesel: formData.get("pesel"),
        fromCity: formData.get("fromCity"),
        toCity: formData.get("toCity"),
        startDate: formData.get("startDate"),
        startTime: formData.get("startTime"),
        endDate: formData.get("endDate"),
        endTime: formData.get("endTime"),
        advancePayment: parseFloat(formData.get("advancePayment")) ? parseFloat(formData.get("advancePayment")) : 0,
        otherExpenses: parseFloat(formData.get("otherExpenses")) ? parseFloat(formData.get("otherExpenses")) : 0,
        dietDto: {
            dailyAllowance: parseFloat(formData.get("dailyAllowance")) ? parseFloat(formData.get("dailyAllowance")) : 45,
            numberOfBreakfasts: parseInt(formData.get("numberOfBreakfasts")) ? parseInt(formData.get("numberOfBreakfasts")) : 0,
            numberOfLunches: parseInt(formData.get("numberOfLunches")) ? parseInt(formData.get("numberOfLunches")) : 0,
            numberOfDinners: parseInt(formData.get("numberOfDinners")) ? parseInt(formData.get("numberOfDinners")) : 0
        },
        overnightStayDto: {
            inputQuantityOfOvernightStayWithoutInvoice: parseInt(formData.get("inputQuantityOfOvernightStayWithoutInvoice")) ? parseInt(formData.get("inputQuantityOfOvernightStayWithoutInvoice")) : 0,
            inputQuantityOfOvernightStayWithInvoice: parseInt(formData.get("inputQuantityOfOvernightStayWithInvoice")) ? parseInt(formData.get("inputQuantityOfOvernightStayWithInvoice")) : 0,
            amountOfTotalOvernightsStayWithInvoice: parseFloat(formData.get("amountOfTotalOvernightsStayWithInvoice")) ? parseFloat(formData.get("amountOfTotalOvernightsStayWithInvoice")) : 0,
            isInvoiceAmountGreaterAllowed: Boolean(form["isInvoiceAmountGreaterAllowed"].checked)
        },
        transportCostDto: {
            inputtedDaysNumberForUndocumentedLocalTransportCost: parseInt(formData.get("inputtedDaysNumberForUndocumentedLocalTransportCost")) ? parseInt(formData.get("inputtedDaysNumberForUndocumentedLocalTransportCost")) : 0,
            documentedLocalTransportCost: parseFloat(formData.get("documentedLocalTransportCost")) ? parseFloat(formData.get("documentedLocalTransportCost")) : 0,
            meansOfTransport: formData.get("meansOfTransport"),
            kilometersByCarEngineUpTo900cc: parseInt(formData.get("kilometersByCarEngineUpTo900cc")) ? parseInt(formData.get("kilometersByCarEngineUpTo900cc")) : 0,
            kilometersByCarEngineAbove900cc: parseInt(formData.get("kilometersByCarEngineAbove900cc")) ? parseInt(formData.get("kilometersByCarEngineAbove900cc")) : 0,
            kilometersByMotorcycle: parseInt(formData.get("kilometersByMotorcycle")) ? parseInt(formData.get("kilometersByMotorcycle")) : 0,
            kilometersByMoped: parseInt(formData.get("kilometersByMoped")) ? parseInt(formData.get("kilometersByMoped")) : 0,
            costOfTravelByPublicTransport: parseFloat(formData.get("costOfTravelByPublicTransport")) ? parseFloat(formData.get("costOfTravelByPublicTransport")) : 0
        }
    };
    console.log(jsonObject);
    const jsonData = JSON.stringify(jsonObject);
    // console.log("Sending data", jsonData);

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
            const errorData = await response.json();
            displayErrorBox("error-box", errorData);

        } else {

            const dataJSON = await response.json();
            localStorage.setItem('resultValues', JSON.stringify(dataJSON));
            window.location.href = 'results.html';
        }

    } catch (error) {
        displayErrorBox("error-box", error);
    }
}
const transportOptions = document.getElementById("meansOfTransport");
transportOptions.addEventListener('change', showSelectedOption);
document.addEventListener('DOMContentLoaded', showSelectedOption);

function showSelectedOption() {
    const formData = new FormData(form);
    const type = formData.get("meansOfTransport");

    const options = {
        public: "costOfTravelByPublicTransport",
        business: "costOfTravelByPublicTransport",
        upTo900: "kilometersByCarEngineUpTo900cc",
        above900: "kilometersByCarEngineAbove900cc",
        motocycle: "kilometersByMotorcycle",
        moped: "kilometersByMoped"

    }
    const labels = document.querySelectorAll('.selectlabel');
    labels.forEach(element => {
        if (!element.classList.contains('hidden')) {
            element.classList.add('hidden')
            element.firstElementChild.value = '';
        }
    })

    const id = options[type];
    const input = document.getElementById(id);
    if (type !== "business") {
        input.parentElement.classList.remove('hidden');
    }
}
