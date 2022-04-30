
document.addEventListener("DOMContentLoaded", function() {
    let accessToken = getCookie().substring(7)

    if (!accessToken || accessToken === "null") {
        window.location.href = '/page/login.html'
    }
})