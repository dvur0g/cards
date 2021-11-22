
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
    const cards = players.find(p => p.username === username).cards;

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
        get("cardHolder" + i + "id").innerHTML = "";
    }
}

function updateCurrentPlayer(player) {
    if (!!player) {
        get("currentPlayer").innerHTML = player.username;
    }
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

    get('gamesList').innerHTML = '';
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

function get(elementId) {
    return document.getElementById(elementId);
}
