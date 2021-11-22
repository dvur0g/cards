
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
            let cardHolder = get("cardHolder" + i);
            cardHolder.style.visibility = "visible";
            cardHolder.innerHTML = cards[i].text;

            get("cardHolder" + i + "Id").innerHTML = cards[i].id;
        }
    }

    for (; i < 10; ++i) {
        hide("cardHolder" + i);
        get("cardHolder" + i + "Id").innerHTML = "";
    }
}

function updateCurrentPlayer(player) {

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

function get(elementId) {
    return document.getElementById(elementId);
}
