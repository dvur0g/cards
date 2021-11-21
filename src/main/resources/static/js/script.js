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
        success: function (data) {
            showResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showResponse(game) {
    updatePlayersList(game);

    if (game.state === "IN_PROGRESS") {

    } else {

    }
}

function updatePlayersList(game) {
    let playersListText = "";
    let i = 0;
    game.players.forEach(player => {
        playersListText += ++i + ". " + player.username + player.score + ", ";
    })
    get("playersList").innerHTML = playersListText;
}

function showAvailableGamesList(games) {
    const list = document.createElement('ul');

    for (let i = 0; i < games.length; i++) {
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
