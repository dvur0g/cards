let currentTimer;

function update(game) {
    updatePlayersList(game.players);
    updateGameState(game.state);
    updateCardHolders(game.players);
    updateSelectedAnswers(game.players);
    updateCurrentPlayer(game.currentPlayer);
    updateQuestion(game.currentQuestion);
    updateTimer(game.timer);
    updateVictoriousAnswer(game.victoriousAnswer);
}

function updatePlayersList(players) {
    let playersListText = "";
    let i = 0;
    players.forEach(player => {
        playersListText += ++i + ". " + player.username + " | " + player.score + ",\ ";
    })
    get("playersList").innerHTML = playersListText;
}

function updateGameState(state) {
    get("gameState").innerHTML = state;
}

function updateCardHolders(players) {
    let cards = players.find(p => p.username === (username)).cards;

    let i = 0;
    if (!!cards) {
        for (; i < cards.length; ++i) {
            visible("cardHolder" + i);
            get("cardHolder" + i + "id").innerHTML = cards[i].id;
            get("cardHolder" + i + "text").innerHTML = cards[i].text;
        }
    }

    for (; i < 10; ++i) {
        hide("cardHolder" + i);
        clear("cardHolder" + i + "id");
    }
}

function updateSelectedAnswers(players) {
    let i = 0;

    players.forEach(player => {
        let selectedAnswer = player.selectedAnswer;

        if (!!selectedAnswer) {
            get("cardSelected" + i + "id").innerHTML = selectedAnswer.id;
            get("cardSelected" + i + "text").innerHTML = selectedAnswer.text;
            get("cardSelected" + i + "userId").innerHTML = player.id;
            visible("cardSelected" + i);
            ++i;
        }
    });

    for (; i < 10; ++i) {
        hide("cardSelected" + i);
        clear("cardSelected" + i + "id");
    }
}

function updateCurrentPlayer(player) {
    if (!!player) {
        get("currentPlayer").innerHTML = player.username;
    }
}

function updateCurrentCredentials() {
    let usernameValue = get("username").value;
    let passwordValue = get("password").value;

    if (isEmpty(usernameValue, "Please enter username")) {
        return false;
    }
    if (isEmpty(passwordValue, "Please enter password")) {
        return false;
    }

    username = usernameValue;
    password = passwordValue;

    get("currentUsername").innerHTML = username;
    return true;
}

function updateQuestion(question) {
    if (!!question) {
        get("question").innerHTML = question.text;
    }
}

function updateTimer(timer) {
    if (!!timer) {
        clearInterval(currentTimer);
        startTimer(timer.countdown);
    }
}

function updateVictoriousAnswer(victoriousAnswer) {
    if (!!victoriousAnswer) {
        get("victoriousAnswer").innerHTML = victoriousAnswer.text;
    } else {
        get("victoriousAnswer").innerHTML = "";
    }
}

function showAvailableGamesList(games) {
    const list = document.createElement('ul');

    for (let i = 0; i < games.length; ++i) {
        const item = document.createElement('li');

        let playersList = ""
        games[i].players.forEach(player => {
            playersList += player.username + ", ";
        })
        playersList = playersList.slice(0, -2)

        item.appendChild(document.createTextNode(games[i].id + " | " + games[i].state + " | [" + playersList + "]"));
        item.onclick = function() {
            connectToGame(games[i].id)
        };

        list.appendChild(item);
    }

    clear('gamesList');
    get('gamesList').appendChild(list);
}

function selectCard(cardHolderIndex) {
    let cardId = get("cardHolder" + cardHolderIndex + "id").innerHTML;

    if (!!cardId) {
        postSelectCard(cardId)
    }
}

function selectAnswer(cardSelectedIndex) {
    let victoriousPlayerId = get("cardSelected" + cardSelectedIndex + "userId").innerHTML;

    if (!!victoriousPlayerId) {
        postSelectAnswer(victoriousPlayerId)
    }
}

function showMenu() {
    get("menu").style.visibility = "visible";
}

function hide(elementId) {
    get(elementId).style.visibility = "hidden";
}

function visible(elementId) {
    get(elementId).style.visibility = "visible";
}

function clearUsername() {
    username = null;
    password = null;
    clear("currentUsername");
    clear("password");
}

function isEmpty(value, message) {
    if (value === null || value === "") {
        alert(message);
        return true;
    } else {
        return false;
    }
}

function startTimer(duration) {
    currentTimer = setInterval(function () {
        get("timer").innerHTML = duration;

        if (--duration < 0) {
            clearInterval(currentTimer);
        }

    }, 1000);
}

function clear(elementId) {
    get(elementId).innerHTML = "";
}

function get(elementId) {
    return document.getElementById(elementId);
}
