$(document).ready(function () {
    // HTML 문서를 로드할 때마다 실행합니다.
    getMessages();
})

function isValidcomment(comment) {
    if (comment == " ") {
        alert('내용을 입력해주세요');
        return false;
    }
    return true;
}
function genRandomName(length) {
    let result = '';
    let characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        let number = Math.random() * charactersLength;
        let index = Math.floor(number);
        result += characters.charAt(index);
    }
    return result;
}
function post() {
    // 1. 작성한 메모를 불러옵니다.
    let comment = $('#textarea-post').val().replace(/(?:\r\n|\r|\n)/g, '<br />');
    // 2. 작성한 메모가 올바른지 isValidContents 함수를 통해 확인합니다.
    if (isValidcomment(comment) == false) {
        return;
    }
    // 3. genRandomName 함수를 통해 익명의 username을 만듭니다.
    let userName = genRandomName(10);
    let placeId = $('#place_id').text();
    // 4. 전달할 data JSON으로 만듭니다.
    let data = {'comment': comment, 'placeId' : placeId};
    // 5. POST /api/memos 에 data를 전달합니다.
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json", // JSON 형식으로 전달함을 알리기
        data: JSON.stringify(data),
        success: function (response) {
            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.reload();
        }
    });

}
function getMessages() {
    // 1. 기존 메모 내용을 지웁니다.
    $('#post-box').empty();
    // 2. 메모 목록을 불러와서 HTML로 붙입니다.
    let placeId = $('#place_id').text();
    $.ajax({
        type: 'GET',
        url: `/comment?placeId=${placeId}`,
        success: function (response) {
            console.log(response);
            for (let i = 0; i < response.length; i++) {
                let message = response[i];
                let id = message['id'];
                let username = message['userNickname'];
                let userEmail = message['userEmail'];
                let comment = message['comment'];
                let time_comment = new Date(message['modifiedAt'])
                let time_past = timePassed(time_comment)
                addHTML(id, username, userEmail, comment, time_past);
            }
        }
    })
}
function addHTML(id, userName, userEmail, comment, time_past) {
    let tempHtml = `<div id="post-box" class="container">
        <div class="box comment-list">
            <article class="media">
                <div class="media-left">
                    <a class="image is-64x64" href="#">
                        <img class="is-rounded" src="profile_pics/profile_placeholder.png">
                    </a>
                </div>
                <div class="media-content">
                    <div class="content">
                        <p>
                            <strong>${userName}</strong> <small>(${userEmail})</small> <small>${time_past}</small>
                            <a type="button" class="delete-comment" onclick="deleteOne('${id}')"><i class="fa fa-trash" aria-hidden="true"></i></a>
                            
                            <br>
                            ${comment}
                        </p>
                    </div>
                </div>
            </article>
        </div>
    </div>`;
    $('#post-box').append(tempHtml);
}
function timePassed(date) {
    let today = new Date()
    let time = (today - date) / 1000 / 60  // 분

    if (time < 1){
        return "방금 전"
    }
    if (time < 60) {
        return parseInt(time) + "분 전"
    }
    time = time / 60  // 시간
    if (time < 24) {
        return parseInt(time) + "시간 전"
    }
    time = time / 24
    if (time < 7) {
        return parseInt(time) + "일 전"
    }
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`
}

function deleteOne(id) {
    // 1. DELETE /api/memos/{id} 에 요청해서 메모를 삭제합니다.
    $.ajax({
        type: "DELETE",
        url: `/comment/${id}`,
        success: function (response) {
            alert('메시지 삭제에 성공하였습니다.');
            window.location.reload();
        }
    })

}