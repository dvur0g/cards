let turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
let turn = "";
let gameOn = false;

function playerTurn(turn, id) {
    if (gameOn) {
        let spotTaken = $("#" + id).text();
        if (spotTaken === "#") {
            makeAMove(playerType, id.split("_")[0], id.split("_")[1]);
        }
    }
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
            gameOn = false;
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function displayResponse(game) {
    let playersList = "";
    let i = 0;
    game.players.forEach(player => {
        playersList += ++i + " " + player.username + player.score + "\n";
    })
    get("playersList").innerHTML = playersList;

    if (game.state === "IN_PROGRESS") {

    } else {

    }

    // let board = game.board;
    // for (let i = 0; i < board.length; i++) {
    //     for (let j = 0; j < board[i].length; j++) {
    //         if (board[i][j] === 1) {
    //             turns[i][j] = 'X'
    //         } else if (board[i][j] === 2) {
    //             turns[i][j] = 'O';
    //         }
    //         let id = i + "_" + j;
    //         $("#" + id).text(turns[i][j]);
    //     }
    // }
    // if (game.winner != null) {
    //     alert("Winner is " + game.winner);
    // }
    // gameOn = true;
}

$(".tic").click(function () {
    let slot = $(this).attr('id');
    playerTurn(turn, slot);
});

function reset() {
    turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
    $(".tic").text("#");
}

$("#reset").click(function () {
    reset();
});

function get(element) {
    return document.getElementById(element);
}
