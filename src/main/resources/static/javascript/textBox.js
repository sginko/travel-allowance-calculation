function displayErrorBox(id, errorText) {
    const errorBox = document.getElementById(id);
    const span = errorBox.querySelector('span');
    span.textContent = 'Error';
    const p = errorBox.querySelector('p');
    p.textContent = `${errorText.message}`;
    errorBox.classList.add('box-animation');
    errorBox.addEventListener('animationend', removeAnimationError);

    function removeAnimationError() {
        errorBox.classList.remove('box-animation');
        return errorBox.removeEventListener('animationend', removeAnimationError);
    }
}

function displayMessageBox(id) {
    const msgBox = document.getElementById(id);
    msgBox.classList.add('box-animation');
    console.log(id, msgBox)
    msgBox.addEventListener('animationend', removeAnimationMsg);

    function removeAnimationMsg() {
        msgBox.classList.remove('box-animation');
        console.log('ada')
        return msgBox.removeEventListener('animationend', removeAnimationMsg);
    }
}