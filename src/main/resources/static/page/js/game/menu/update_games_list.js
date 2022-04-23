
document.addEventListener("DOMContentLoaded", function() {
    getAvailableGames()
})

const interval = setInterval(function() {
    getAvailableGames()
}, 5000);
