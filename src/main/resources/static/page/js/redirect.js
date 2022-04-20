
document.addEventListener("DOMContentLoaded", function() {
    if (getCookie() === null) {
        window.location.href = '/page/login.html'
    }
})