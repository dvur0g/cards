const url = 'http://localhost:8080';
let stompClient;

let gameId;

let username = null;
let password = null;

function connectToSocket(game) {
    console.log("connecting to the game " + game.id);
    
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + game.id, function (response) {
            let data = JSON.parse(response.body);
            update(data);
        })
    })

    get("menu").style.visibility = "hidden";
    update(game);
}

function disconnectFromGame() {
    console.log("disconnect from game " + username);

    $.ajax({
        url: url + "/game/disconnect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        headers: {
            "Authorization": auth()
        },
        data: JSON.stringify({
            "username": username
        })
    });
    stompClient.disconnect();

    clearUsername();
}

function createGame() {
    if (!updateCurrentUsername()) {
        return;
    }

    $.ajax({
        url: url + "/game/create",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        headers: {
            "Authorization": auth()
        },
        data: JSON.stringify({
            "username": username
        }),
        success: function (game) {
            gameId = game.id;
            connectToSocket(game);
            alert("Your created a game. Game id is: " + game.id);
            },
        error: function (error) {
            console.log(error);
        }
    })
}

function connectToGame(gameId) {
    if (gameId == null || gameId === '') {
        alert("Please enter game id");
        return;
    }

    if (!updateCurrentUsername()) {
        return;
    }

    $.ajax({
        url: url + "/game/connect",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        headers: {
            "Authorization": auth()
        },
        data: JSON.stringify({
            "player": {
                "username": username
            },
            "gameId": gameId
        }),
        success: function (game) {
            connectToSocket(game);
            },
        error: function (error) {
            console.log(error);
        }
    })
}

function getAvailableGames() {
    $.ajax({
        url: url + "/game/list",
        headers: {
            'Authorization': auth()
        },
        type: 'GET',
        success: function (gamesList) {
            showAvailableGamesList(gamesList)
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function auth() {
    return JSON.stringify({
        "username": username,
        "password": password
    });
}