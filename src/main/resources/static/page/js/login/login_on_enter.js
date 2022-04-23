
document.getElementById('password').addEventListener('keyup',function(event) {
    if (event.code === "Enter") {
        doLogin()
    }
});