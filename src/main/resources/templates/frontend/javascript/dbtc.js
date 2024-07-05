
function validateFormFields() {
    const dbtc = document.forms["dbtc-form"];

    if (dbtc["numberOfBreakfasts"].value === "") {
        dbtc["numberOfBreakfasts"].value = 0;
    }
    if (dbtc["numberOfLunches"].value === "") {
        dbtc["numberOfLunches"].value = 0;
    }
    if (dbtc["numberOfDinners"].value === "") {
        dbtc["numberOfDinners"].value = 0;
    }
    console.log("from submited",dbtc["pesel"].value, dbtc["numberOfLunches"].value, dbtc["numberOfBreakfasts"].value, dbtc["numberOfDinners"].value);
}

