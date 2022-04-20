const COOKIE_NAME = "CARDS_COOKIE";

function doLogin() {
    const username = get('login').value;
    const password = get('password').value;

    $.ajax({
        url: url + "/auth/login",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "username": username,
            "password": password
        }),
        success: function (response) {
            setCookie(response.access_token)

            console.log('accessToken cookie: ' + getCookie())
            window.location.href = '/page/gameplay.html'
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function logout() {
    eraseCookie()

    window.location.href = '/page/login.html'
}

function setCookie(accessToken) {
    const days = 10;
    let expires = "";

    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }

    document.cookie = COOKIE_NAME + "=" + (accessToken || "")  + expires + "; path=/";
}

function getCookie() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${COOKIE_NAME}=`);
    if (parts.length === 2) {
        return  "Bearer " + parts.pop().split(';').shift();
    }
}

function eraseCookie() {
    document.cookie = COOKIE_NAME +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}