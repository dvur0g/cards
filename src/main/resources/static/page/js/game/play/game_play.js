let currentTimer;
let gameId = null

let gameStateHints = null

document.addEventListener("DOMContentLoaded", function() {
    gameId = location.search.substring(1).split("=")[1];
    connectToSocket(gameId)
    getGame(gameId)

    getGameStateHints()
})

function getGame(gameId) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId
        }),
        success: function (game) {
            update(game)
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function getGameStateHints() {
    $.ajax({
        url: url + "/dictionary/game-state-hints",
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (body) {
            gameStateHints = new Map(Object.entries(body.gameStateHintMap))
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function update(game) {
    updatePlayersList(game);
    updateGameState(game);
    updateCardHolders(game.players);
    updateSelectedAnswers(game);
    updateCurrentPlayer(game.currentPlayer);
    updateQuestion(game);
    updateTimer(game.timer);
    updateVictoriousAnswer(game.victoriousAnswer);
}

function updatePlayersList(game) {
    const players = game.players

    const list = get("playersList")
    clear("playersList");

    if (game.state === "FINISHED") {
        list.classList.add("finish")
    }

    players.forEach(player => {
        const item = document.createElement('li');
        item.className = "player-name"

        const div = document.createElement("div");
        div.className = "player-name-number"
        div.appendChild(document.createTextNode(" " + player.score))

        item.appendChild(document.createTextNode(player.username))
        item.appendChild(div)

        if (game.state === "FINISHED" && player.id === game.winner.id) {
            item.classList.add("winner")
        }

        list.appendChild(item);
    })
}

function updateGameState(game) {
    let hint = gameStateHints.get(game.state);
    if (game.state === "SHOW_VICTORIOUS_ANSWER") {
        hint = hint.replace("____", game.victoriousPlayer.username);
    } else if (game.state === "FINISHED") {
        hint = hint.replace("____", game.winner.username)
    }

    get("hint").innerHTML = hint
}

function updateCardHolders(players) {
    let cards = players.find(p => p.username === (username)).cards;

    let i = 0;
    if (!!cards) {
        for (; i < cards.length; ++i) {
            visible("cardHolder" + i);
            get("cardHolder" + i + "id").innerHTML = cards[i].id;
            get("cardHolder" + i).innerHTML = cards[i].text;
        }
    }

    for (; i < 10; ++i) {
        hide("cardHolder" + i);
        clear("cardHolder" + i + "id");
    }
}

function updateSelectedAnswers(game) {
    const players = game.players;

    let i = 0;

    if (game.state === "SHOW_VICTORIOUS_ANSWER") {
        for (; i < 10; ++i) {
            const answerUserId = get("cardSelected" + i + "userId").innerHTML

            if (!(answerUserId === "")) {
                let card = get("cardSelected" + i)
                if (answerUserId == game.victoriousPlayer.id) {
                    card.classList.add("win")
                } else {
                    card.classList.add("lose")
                }
            }
        }
        return
    }

    players.forEach(player => {
        let selectedAnswer = player.selectedAnswer;

        if (!!selectedAnswer) {
            get("cardSelected" + i + "id").innerHTML = selectedAnswer.id;
            get("cardSelected" + i).innerHTML = selectedAnswer.text;
            get("cardSelected" + i).classList.remove("win")
            get("cardSelected" + i).classList.remove("lose")
            get("cardSelected" + i + "userId").innerHTML = player.id;
            visible("cardSelected" + i);
            ++i;
        }
    });

    for (; i < 10; ++i) {
        hide("cardSelected" + i);
        clear("cardSelected" + i + "id");
        clear("cardSelected" + i + "userId");
    }
}

function updateCurrentPlayer(currentPlayer) {
    if (!!currentPlayer) {
        get("currentPlayer").innerHTML = currentPlayer.username;
    }
}

function updateQuestion(game) {
    if (game.state === "SELECTING_ANSWERS") {
        if (!!game.currentQuestion) {
            get("question").innerHTML = game.currentQuestion.text;
        }
    }
}

function updateTimer(timer) {
    if (!!timer) {
        clearInterval(currentTimer);
        startTimer(timer.countdown);
    }
}

function updateVictoriousAnswer(victoriousAnswer) {
    let question = get('question');
    let questionText = question.innerHTML;

    if (!!victoriousAnswer) {
        if (questionText.includes("____")) {
            questionText = questionText.replace("____", "<div class=\"victorious-answer\">" + victoriousAnswer.text + "</div>")
            question.innerHTML = questionText
        } else {
            get("victoriousAnswer").innerHTML = victoriousAnswer.text;
        }
    } else {
        get("victoriousAnswer").innerHTML = "";
    }
}

function disconnectFromGame() {
    console.log("disconnect from game " + username);

    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
    });
    stompClient.disconnect();

    window.location.href = '/page/gameMenu.html'
}

function selectCard(cardHolderIndex) {
    let cardId = get("cardHolder" + cardHolderIndex + "id").innerHTML;

    if (!!cardId) {
        postSelectCard(cardId)
    }
}

function postSelectCard(cardId) {
    $.ajax({
        url: url + "/game/select-card",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
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

function selectAnswer(cardSelectedIndex) {
    let victoriousPlayerId = get("cardSelected" + cardSelectedIndex + "userId").innerHTML;

    if (!!victoriousPlayerId) {
        postSelectAnswer(victoriousPlayerId)
    }
}

function postSelectAnswer(victoriousPlayerId) {
    $.ajax({
        url: url + "/game/select-victorious-answer",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
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

function startTimer(duration) {
    currentTimer = setInterval(function () {
        get("timer").innerHTML = --duration;

        if (duration < 0) {
            clearInterval(currentTimer);
        }

    }, 1000);
}

window.onbeforeunload = disconnectFromGameBeforeClosingPage;
function disconnectFromGameBeforeClosingPage() {
    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST',
        headers: {
            "Authorization": getCookie()
        },
    });
    stompClient.disconnect();
}

function connectToSocket(gameId) {
    console.log("connecting to the game " + gameId);

    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({'Authorization': getCookie()}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);

            update(data);
        }, {'Authorization': getCookie()})
    })
}

