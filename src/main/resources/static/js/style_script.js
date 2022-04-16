
function blur(elementId) {
    const element = document.getElementById(elementId);
    element.style.filter = "blur(5px)"
}

function unblur(elementId) {
    const element = document.getElementById(elementId);
    element.style.filter = "blur(0px)"
}