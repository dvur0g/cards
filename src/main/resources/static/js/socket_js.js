const url = 'http://localhost:8080';
let stompClient;

let gameId;
let username;
let game;

function connectToSocket(game) {
    console.log("connecting to the game " + game.id);
    
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + game.id, function (response) {
            console.log("SUBSCRIBED")
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
        data: JSON.stringify({
            "username": username
        })
    });
    stompClient.disconnect();
}

function createGame() {
    let usernameValue = get("username").value;

    if (usernameValue == null || usernameValue === '') {
        alert("Please enter username");
        return;
    }
    
    username = usernameValue;
    $.ajax({
        url: url + "/game/create",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
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
    let usernameValue = get("username").value;

    if (usernameValue == null || usernameValue === '') {
        alert("Please enter username");
        return;
    }

    if (gameId == null || gameId === '') {
        alert("Please enter game id");
        return;
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
        type: 'GET',
        success: function (gamesList) {
            showAvailableGamesList(gamesList)
        },
        error: function (error) {
            console.log(error);
        }
    })
}
