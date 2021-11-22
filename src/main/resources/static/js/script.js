
function update(game) {
    updatePlayersList(game.players);
    updateGameState(game.state);
    updateCardHolders(game.players);
    updateCurrentPlayer(game.currentPlayer);

    if (game.state === "IN_PROGRESS") {

    } else {

    }
}

function updatePlayersList(players) {
    let playersListText = "";
    let i = 0;
    players.forEach(player => {
        playersListText += ++i + ". " + player.username + player.score + ", ";
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

function showAvailableGamesList(games) {
    const list = document.createElement('ul');

    for (let i = 0; i < games.length; ++i) {
        const item = document.createElement('li');

        item.appendChild(document.createTextNode(games[i].id + ", " + games[i].state));
        item.onclick = function() {
            connectToGame(games[i].id)
        };

        list.appendChild(item);
    }

    clear('gamesList');
    get('gamesList').appendChild(list);
}

function selectCard(cardHolderIndex) {


    hide("cardHolder" + cardHolderIndex);
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

function clear(elementId) {
    get(elementId).innerHTML = "";
}

function get(elementId) {
    return document.getElementById(elementId);
}
