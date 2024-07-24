document.addEventListener('DOMContentLoaded', addValues);

function addValues() {

    let storedValues = JSON.parse(localStorage.getItem('resultValues'));
    if (storedValues === null) {
        alert('Brak wartości z kalkulatora, ładowanie z obiektu');
        const pjson = `{"id": 1,"pesel": 12345678901, "fromCity": "Warsaw", "toCity": "Krakow", "startDate": "2023-01-01", "startTime": "08:00:00", "endDate": "2023-01-02", "endTime": "18:00:00", "totalAmount": 300, "advancePayment": 100, "dietResponse": {"id": 1,"dietAmount": 135, "numberOfBreakfasts": 1, "numberOfLunches": 1, "numberOfDinners": 1}, "overnightStayResponseDto": {"id": 1, "inputQuantityOfOvernightStayWithoutInvoice": 1, "amountOfTotalOvernightsStayWithoutInvoice": 50, "inputQuantityOfOvernightStayWithInvoice": 1,"amountOfTotalOvernightsStayWithInvoice": 100, "overnightStayAmount": 150}}`
        storedValues = JSON.parse(pjson);
    }
    console.log(storedValues);


    for (let prop in storedValues) {
        const objectAsProperty = storedValues[prop];
        if (typeof objectAsProperty === 'object') {

            for (let prop2 in objectAsProperty) {
                // console.log("Drugi lvl " + prop2, objectAsProperty[prop2]);
                if (prop2 !== 'id') setSpanTextContent(objectAsProperty[prop2], prop2);
            }

        } else {
            //console.log("Pierwszy lvl " + prop, storedValues[prop]);
            setSpanTextContent(storedValues[prop], prop);
        }
    }
    const inputToPrint = document.getElementById('input-print-id');
    inputToPrint.value = storedValues.id;
    localStorage.clear();
}

function setSpanTextContent(object, prop) {
    const spanId = `span-${prop}`;
    const spanTag = document.getElementById(spanId);
    spanTag.textContent = object;
}

const formToPrint = document.getElementById('form-print-id');
formToPrint.addEventListener('submit', fetchPdfDocument);

async function fetchPdfDocument(evt) {
    evt.preventDefault();
    const formToPrintData = new FormData(formToPrint);
    const formBody = JSON.stringify(Object.fromEntries(formToPrintData.entries()));
    console.log(formBody);

    try {
        const response = await fetch(`http://localhost:8080/api/v1/travels/print/${formToPrintData.get('id')}`, {
            method: 'POST',
            headers: {
                'Accept': 'application/pdf',
                'Content-type': 'application/json'
            },
            body: formBody
        });
        if (response.ok) {
            const errorMessage = await response.text();
            alert(`Error: ${errorMessage}`);
            throw new Error(errorMessage);
        }
        const data = await response.blob();

        const url = window.URL.createObjectURL(data);
        const pdfWindow = window.open(url);

        const interval = setInterval(() => {
            if (pdfWindow.document.readyState === 'complete') {
                clearInterval(interval);
                pdfWindow.print();
            }
        }, 500);

    } catch (error) {
        console.error('Błąd:', error);
        alert('Wystąpił błąd podczas wysyłania żadąnia o wydruk dokumentu pdf');
    }
}