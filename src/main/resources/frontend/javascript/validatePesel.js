const peselField = document.getElementById("pesel-field1") || document.getElementById("pesel-field2");;
peselField.addEventListener('input', validateNotNumbers);

function validateNotNumbers() {
    let peselValue = peselField.value;
    const notNumbers = peselValue.match(/[^0-9]/);
    if (notNumbers !== null) {
        const cutPeselValue = peselValue.slice(0, peselValue.length - 1);
        return peselField.value = cutPeselValue;
    }
}