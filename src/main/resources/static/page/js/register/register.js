
function register() {
    const name = get('name').value;
    const surname = get('password').value;
    const email = get('email').value;
    const username = get('login').value;
    const password = get('password').value;
    const repeatedPassword = get('password').value;

    $.ajax({
        url: url + "/auth/register",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": name,
            "surname": surname,
            "email": email,
            "password": password,
            "username": username,
            "repeatedPassword": repeatedPassword,
        }),
        success: function (response) {
            window.location.href = '/page/login.html'
        },
        error: function (error) {
            console.log(error);
        }
    })
}