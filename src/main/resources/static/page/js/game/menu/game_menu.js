let currentGameId = null

function toggleNewGame() {
    get("box-new-game").style.visibility = "visible"
    get("box-connect").style.visibility = "hidden"
}

function getAvailableGames() {
    $.ajax({
        url: url + "/game/list",
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (gamesList) {
            showAvailableGamesList(gamesList)
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showAvailableGamesList(games) {
    const list = get('gamesList');
    clear('gamesList');

    for (let i = 0; i < games.length; ++i) {
        const item = document.createElement('li');
        item.className = "game-name"

        item.appendChild(document.createTextNode(games[i].name));
        item.onclick = function() {
            openPlayerList(games[i])
        };

        list.appendChild(item);
    }
}

function openPlayerList(game) {
    currentGameId = game.id

    const list = get('playerScoreList');
    clear('playerScoreList');

    get("gameRoundNumber").innerText = game.round


    game.players.forEach(player => {
        const item = document.createElement('li');
        item.className = "player-name"

        const div = document.createElement("div");
        div.className = "player-score"
        div.appendChild(document.createTextNode(" " + player.score))

        item.appendChild(document.createTextNode(player.username))
        item.appendChild(div)

        list.appendChild(item);
    })

    get("box-new-game").style.visibility = "hidden"
    get("box-connect").style.visibility = "visible"
}

function createGame() {
    const gameName = get("gameNameInput").value
    if (isEmpty(gameName, "Введите название игры!")) {
        return
    }

    $.ajax({
        url: url + "/game/create",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": gameName
        }),
        success: function (game) {
            window.location.href = '/page/gamePlay.html?gameId=' + game.id
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function connectToGame() {
    $.ajax({
        url: url + "/game/connect",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": currentGameId
        }),
        success: function (game) {
            window.location.href = '/page/gamePlay.html?gameId=' + game.id
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function suggestAnswer() {
    const textArea = document.getElementById("suggestCardText")
    const suggestedCardText = textArea.value
    if (isEmpty(suggestedCardText, "Введите текст карты!")) {
        return
    }

    $.ajax({
        url: url + "/answer/suggest",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "text": suggestedCardText
        }),
        error: function (error) {
            console.log(error);
        }
    })

    textArea.value = ""
}

function suggestQuestion() {
    const textArea = document.getElementById("suggestedQuestionText")
    const suggestedQuestionText = textArea.value
    if (isEmpty(suggestedQuestionText, "Введите текст вопроса!")) {
        return
    }

    $.ajax({
        url: url + "/question/suggest",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
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
