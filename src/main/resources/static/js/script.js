let turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
let turn = "";

function playerTurn(turn, id) {
    // if (gameOn) {
    //     let spotTaken = $("#" + id).text();
    //     if (spotTaken === "#") {
    //         makeAMove(playerType, id.split("_")[0], id.split("_")[1]);
    //     }
    // }
}

function makeAMove(type, xCoordinate, yCoordinate) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordinateX": xCoordinate,
            "coordinateY": yCoordinate,
            "gameId": gameId
        }),
        success: function (game) {
            update(game);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function update(game) {
    updatePlayersList(game.players);
    updateGameState(game.state);
    updateCardHolders(game.players);
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
            let element = get("cardHolder" + i);
            element.style.visibility = "visible";
            element.innerHTML = cards[i].text;

            console.log(Object.getOwnPropertyNames(cards[i]));
            console.log("card " + i + ": [" + cards[i].id + ", " + cards[i].text + ", " + cards[i].type + "]");
        }
    }

    for (; i < 10; ++i) {
        get("cardHolder" + i).style.visibility = "hidden";
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

    document.getElementById('gamesList').innerHTML = '';
    document.getElementById('gamesList').appendChild(list);
}

function showMenu() {
    get("menu").style.visibility = "visible";
}

function get(element) {
    return document.getElementById(element);
}
