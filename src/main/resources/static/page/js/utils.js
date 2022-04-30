
function hide(elementId) {
    get(elementId).style.visibility = "hidden";
}

function visible(elementId) {
    get(elementId).style.visibility = "visible";
}

function isEmpty(value, message) {
    if (value === null || value === "") {
        alert(message);
        return true;
    } else {
        return false;
    }
}

function clear(elementId) {
    get(elementId).innerHTML = "";
}

function get(elementId) {
    return document.getElementById(elementId);
}
