const url = 'http://localhost:8080';
let stompClient;

let username = null;
let roles = null
let gameId = null;

document.addEventListener("DOMContentLoaded", function() {
    $.ajax({
        url: url + "/me",
        type: 'GET',
        success: function (me) {
            username = me.username
            roles = new Set(me.roles)

            get("currentUsername").innerHTML = username
            if (roles.has("ADMIN")) {
                get("div-admin").style.visibility = "visible"
            } else {
                get("div-admin").style.visibility = "hidden"
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
});

window.onbeforeunload = disconnectFromGameBeforeClosingPage;
function disconnectFromGameBeforeClosingPage() {
    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST'
    });
    stompClient.disconnect();
}

function connectToSocket(game) {
    console.log("connecting to the game " + game.id);
    
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + game.id, function (response) {
            let data = JSON.parse(response.body);

            update(data);
        })
    })

    get("menu").style.visibility = "hidden";
    gameId = game.id;

    update(game);
}

function disconnectFromGame() {
    console.log("disconnect from game " + username);

    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST'
    });
    stompClient.disconnect();
}

function createGame() {
    $.ajax({
        url: url + "/game/create",
        type: 'POST',
        success: function (game) {
            connectToSocket(game);
            alert("Your created a game. Game id is: " + game.id);
            },
        error: function (error) {
            console.log(error);
        }
    })

    unblur('playingArea')
}

function connectToGame(gameId) {
    $.ajax({
        url: url + "/game/connect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "username": username,
            "gameId": gameId
        }),
        success: function (game) {
            connectToSocket(game);
            },
        error: function (error) {
            console.log(error);
        }
    })

    unblur('playingArea')
}

function getAvailableGames() {
    $.ajax({
        url: url + "/game/list",
        type: 'GET',
        success: function (gamesList) {
            showAvailableGamesList(gamesList)
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function postSelectCard(cardId) {
    $.ajax({
        url: url + "/game/select-card",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "cardId": cardId,
            "gameId": gameId
        }),
        error: function (error) {
            console.log(error);
        }
    })
}

function postSelectAnswer(victoriousPlayerId) {
    $.ajax({
        url: url + "/game/select-victorious-answer",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "victoriousPlayerId": victoriousPlayerId,
            "gameId": gameId
        }),
        error: function (error) {
            console.log(error);
        }
    })
}

function suggestAnswer() {
    const textArea = document.getElementById("suggest-answer")
    const suggestedAnswerText = textArea.value
    if (isEmpty(suggestedAnswerText, "Введите текст карты!")) {
        return
    }

    $.ajax({
        url: url + "/answer/suggest",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "text": suggestedAnswerText
        }),
        error: function (error) {
            console.log(error);
        }
    })

    textArea.value = ""
}

function suggestQuestion() {
    const textArea = document.getElementById("suggest-question")
    const suggestedQuestionText = textArea.value
    if (isEmpty(suggestedQuestionText, "Введите текст вопроса!")) {
        return
    }

    $.ajax({
        url: url + "/question/suggest",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "text": suggestedQuestionText
        }),
        error: function (error) {
            console.log(error);
        }
    })

    textArea.value = ""
}

function addAnswer() {
    const textArea = document.getElementById("add-answer")
    const addAnswerText = textArea.value
    if (isEmpty(addAnswerText, "Введите текст карты!")) {
        return
    }

    $.ajax({
        url: url + "/answer/add",
        type: 'POST',
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
