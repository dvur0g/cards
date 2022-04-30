let page = 0;
let count = 12

let menu = 2;

document.addEventListener("DOMContentLoaded", function() {
    show()
})

function prevPage() {
    if (page < 1) {
        return
    }

    --page;
    show()
}

function nextPage() {
    ++page;
    show()
}

function showNew(newMenu) {
    menu = newMenu
    page = 0
    show()
}

function show() {
    if (menu === 0) {
        showAnswers()
    } else if (menu === 1) {
        showQuestions()
    } else if (menu === 2) {
        showSuggestedAnswers()
    } else if (menu === 3) {
        showSuggestedQuestions()
    }

    get('number').innerText = (page < 10) ? '0' + (page + 1) : (page + 1);
}

function showAnswers() {
    get('list-game').innerText = "Список карт"

    $.ajax({
        url: url + "/answer/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = get('admin-list');
            clear('admin-list')

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');
                item.classList.add('random-name')

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date.toString().substring(0, 16)));

                const buttonDelete = document.createElement("button");
                buttonDelete.classList.add("button-no")
                buttonDelete.textContent = "УДАЛИТЬ";
                buttonDelete.onclick = function () {
                    $.ajax({
                        url: url + "/answer/delete/" + cardList[i].id,
                        type: 'DELETE',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            showAnswers();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                item.appendChild(buttonDelete)
                list.appendChild(item)
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showQuestions() {
    get('list-game').innerText = "Список вопросов"

    $.ajax({
        url: url + "/question/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = get('admin-list');
            clear('admin-list')

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');
                item.classList.add('random-name')

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date.toString().substring(0, 16)));

                const buttonDelete = document.createElement("button");
                buttonDelete.classList.add("button-no")
                buttonDelete.textContent = "УДАЛИТЬ";
                buttonDelete.onclick = function () {
                    $.ajax({
                        url: url + "/question/delete/" + cardList[i].id,
                        type: 'DELETE',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            show();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                item.appendChild(buttonDelete)
                list.appendChild(item)
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showSuggestedAnswers() {
    get('list-game').innerText = "Список предложенных карт"

    $.ajax({
        url: url + "/answer/suggested/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = get('admin-list');
            clear('admin-list')

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');
                item.classList.add('random-name')

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date.toString().substring(0, 16)));

                const buttonApprove = document.createElement("button");
                buttonApprove.classList.add("button-yes")
                buttonApprove.textContent = "ПРИНЯТЬ";
                buttonApprove.onclick = function () {
                    $.ajax({
                        url: url + "/answer/suggest/approve/" + cardList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            showSuggestedAnswers();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                const buttonDisapprove = document.createElement("button");
                buttonDisapprove.classList.add("button-no")
                buttonDisapprove.textContent = "ОТКЛОНИТЬ";
                buttonDisapprove.onclick = function () {
                    $.ajax({
                        url: url + "/answer/suggest/disapprove/" + cardList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            show();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                item.appendChild(buttonApprove)
                item.appendChild(buttonDisapprove)
                list.appendChild(item);
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showSuggestedQuestions() {
    get('list-game').innerText = "Список предложенных вопросов"

    $.ajax({
        url: url + "/question/suggested/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (questionList) {
            const list = get('admin-list');
            clear('admin-list')

            for (let i = 0; i < questionList.length; ++i) {
                const item = document.createElement('li');
                item.classList.add("random-name")
                item.appendChild(document.createTextNode(questionList[i].text + " | "
                    + questionList[i].username + " | " + questionList[i].date.toString().substring(0, 16)));

                const buttonApprove = document.createElement("button");
                buttonApprove.classList.add("button-yes")
                buttonApprove.textContent = "ПРИНЯТЬ";
                buttonApprove.onclick = function () {
                    $.ajax({
                        url: url + "/question/suggest/approve/" + questionList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            show();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                const buttonDisapprove = document.createElement("button");
                buttonDisapprove.classList.add("button-no")
                buttonDisapprove.textContent = "ОТКЛОНИТЬ";
                buttonDisapprove.onclick = function () {
                    $.ajax({
                        url: url + "/question/suggest/disapprove/" + questionList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            show();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };


                item.appendChild(buttonApprove)
                item.appendChild(buttonDisapprove)
                list.appendChild(item);
            }
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function goBack() {
    window.location.href = '/page/gameMenu.html'
}
