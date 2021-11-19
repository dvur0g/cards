const url = 'http://localhost:8080';
let stompClient;
let gameId;
let username;
let playerType;

function showMenu() {
    document.getElementById("menu").style.visibility = "visible";
}

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })

    document.getElementById("menu").style.visibility = "hidden";
}

function disconnectFromGame() {
    console.log(username);

    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "username": username
        })
    });
    stompClient.disconnect();
}

function createGame() {
    let usernameValue = document.getElementById("username").value;

    if (usernameValue == null || usernameValue === '') {
        alert("Please enter username");
    } else {
        username = usernameValue;
        console.log(usernameValue + username);

        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "username": username
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                reset();
                connectToSocket(gameId);
                alert("Your created a game. Game id is: " + data.gameId);
                gameOn = true;
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function connectToRandom() {
    let usernameValue = document.getElementById("username").value;

    if (usernameValue == null || usernameValue === '') {
        alert("Please enter username");
    } else {
        username = usernameValue;

        $.ajax({
            url: url + "/game/connect/random",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "username": username
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.username);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function connectToSpecificGame(gameId) {
    let usernameValue = document.getElementById("username").value;

    if (usernameValue == null || usernameValue === '') {
        alert("Please enter username");
    } else {
        if (gameId == null || gameId === '') {
            alert("Please enter game id");
        }

        username = usernameValue;

        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "player": {
                    "username": username
                },
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                reset();
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1.username);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function getAvailableGamesList() {
    $.ajax({
        url: url + "/game/list",
        type: 'GET',
        success: function (gamesList) {
            showUL(gamesList)
            gamesList.forEach(element => {
                console.log(element.id + ", " + element.minPlayersAmount + ", " + element.state)

            });
            },
        error: function (error) {
            console.log(error);
        }
    })
}

function showUL(array) {
    const list = document.createElement('ul');

    for (let i = 0; i < array.length; i++) {
        const item = document.createElement('li');

        item.appendChild(document.createTextNode(array[i].id + ", " + array[i].state));
        item.onclick = function() {
            connectToSpecificGame(array[i].id)
        };

        list.appendChild(item);
    }

    document.getElementById('gamesList').appendChild(list);
}

