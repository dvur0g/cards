let isDeleted = false;

function showAnswers() {
    const page = get("page").value;
    const count = get("count").value;

    if (isEmpty(page, "Введите номер страницы!") || isEmpty(count, "Введите кол-во записей!")) {
        return
    }

    $.ajax({
        url: url + "/answer/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = document.createElement('ul');

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date));

                const buttonDelete = document.createElement("button");
                buttonDelete.textContent = "Удалить";
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

                list.appendChild(item);
                list.appendChild(buttonDelete)
            }

            clear('admin-list');
            get('admin-list').appendChild(list);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showQuestions() {
    const page = get("page").value;
    const count = get("count").value;

    if (isEmpty(page, "Введите номер страницы!") || isEmpty(count, "Введите кол-во записей!")) {
        return
    }

    $.ajax({
        url: url + "/question/list?page=" + page + "&count=" + count,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = document.createElement('ul');

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date));

                const buttonDelete = document.createElement("button");
                buttonDelete.textContent = "Удалить";
                buttonDelete.onclick = function () {
                    $.ajax({
                        url: url + "/question/delete/" + cardList[i].id,
                        type: 'DELETE',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            showQuestions();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                list.appendChild(item);
                list.appendChild(buttonDelete)
            }

            clear('admin-list');
            get('admin-list').appendChild(list);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showSuggestedAnswers() {
    const page = get("page").value;
    const count = get("count").value;

    if (isEmpty(page, "Введите номер страницы!") || isEmpty(count, "Введите кол-во записей!")) {
        return
    }

    $.ajax({
        url: url + "/answer/suggested/list?page=" + page + "&count=" + count + "&isDeleted=" + isDeleted,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (cardList) {
            const list = document.createElement('ul');

            for (let i = 0; i < cardList.length; ++i) {
                const item = document.createElement('li');

                item.appendChild(document.createTextNode(cardList[i].text + " | "
                    + cardList[i].username + " | " + cardList[i].date));

                const buttonApprove = document.createElement("button");
                buttonApprove.textContent = "Принять";
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
                buttonDisapprove.textContent = "Отклонить";
                buttonDisapprove.onclick = function () {
                    $.ajax({
                        url: url + "/answer/suggest/disapprove/" + cardList[i].id,
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

                list.appendChild(item);
                item.appendChild(buttonApprove)
                item.appendChild(buttonDisapprove)
            }

            clear('admin-list');
            get('admin-list').appendChild(list);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function showSuggestedQuestions() {
    const page = get("page").value;
    const count = get("count").value;

    if (isEmpty(page, "Введите номер страницы!") || isEmpty(count, "Введите кол-во записей!")) {
        return
    }

    $.ajax({
        url: url + "/question/suggested/list?page=" + page + "&count=" + count + "&isDeleted=" + isDeleted,
        type: 'GET',
        headers: {
            "Authorization": getCookie()
        },
        success: function (questionList) {
            const list = document.createElement('ul');

            for (let i = 0; i < questionList.length; ++i) {
                const item = document.createElement('li');

                item.appendChild(document.createTextNode(questionList[i].text + " | "
                    + questionList[i].username + " | " + questionList[i].date));

                const buttonApprove = document.createElement("button");
                buttonApprove.textContent = "Принять";
                buttonApprove.onclick = function () {
                    $.ajax({
                        url: url + "/question/suggest/approve/" + questionList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            showSuggestedQuestions();
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    })
                };

                const buttonDisapprove = document.createElement("button");
                buttonDisapprove.textContent = "Отклонить";
                buttonDisapprove.onclick = function () {
                    $.ajax({
                        url: url + "/question/suggest/disapprove/" + questionList[i].id,
                        type: 'POST',
                        headers: {
                            "Authorization": getCookie()
                        },
                        success: function () {
                            showSuggestedQuestions();
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

            clear('admin-list');
            get('admin-list').appendChild(list);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function filterDeleted() {
    isDeleted = true;
}

function filterWithoutDeleted() {
    isDeleted = false;
}