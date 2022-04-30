const url = 'http://localhost:8181';
let stompClient;

let username = null;
let roles = null

document.addEventListener("DOMContentLoaded", function() {
    $.ajax({
        url: url + "/auth/me",
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (me) {
            username = me.username
            roles = new Set(me.roles)

            get("currentUsername").innerHTML = username
            if (roles.has("ADMIN")) {
                // get("div-admin").style.visibility = "visible"
            } else {
                // get("div-admin").style.visibility = "hidden"
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
});

function addAnswer() {
    const textArea = document.getElementById("add-answer")
    const addAnswerText = textArea.value
    if (isEmpty(addAnswerText, "Введите текст карты!")) {
        return
    }

    $.ajax({
        url: url + "/answer/add",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "text": addAnswerText
        }),
        error: function (error) {
            console.log(error);
        }
    })

    textArea.value = ""
}

function addQuestion() {
    const textArea = document.getElementById("add-question")
    const addQuestionText = textArea.value
    if (isEmpty(addQuestionText, "Введите текст вопроса!")) {
        return
    }

    $.ajax({
        url: url + "/question/add",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "text": addQuestionText
        }),
        error: function (error) {
            console.log(error);
        }
    })

    textArea.value = ""
}
