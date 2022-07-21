$(document).ready(function () {
    // HTML 문서를 로드할 때마다 실행
    getMessages();
})

function isValidcomment(comment) {
    if (comment == " ") {
        alert('내용을 입력해주세요');
        return false;
    }
    return true;
}

function post() {
    // 작성한 메모
    let comment = $('#textarea-post').val().replace(/(?:\r\n|\r|\n)/g, '<br />');
    // 작성한 메모가 올바른지 isValidContents 함수를 통해 확인
    if (isValidcomment(comment) == false) {
        return;
    }

    let communityAddr = $('#community-addr').text();
    // 전달할 data JSON으로 만듬
    let data = {'comment': comment, 'communityAddr': communityAddr};
    // POST /api/memos 에 data를 전달
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('댓글작성 완료');
            window.location.reload();
        },
        error: function (response) {
            alert("GUEST 유저입니다.")
        },
    });

}

function getMessages() {
    // 기존 메모 내용을 지웁니다.
    $('#post-box').empty();
    // 메모 목록을 불러와서 HTML로 붙입니다.
    let communityAddr = $('#community-addr').text();
    $.ajax({
        type: 'GET',
        url: `/comment?communityAddr=${communityAddr}`,
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
                let userProfileImg = message['userProfileImg'];
                let userId = message['userId'];

                console.log(comment)

                addHTML(id, username, userEmail, comment, time_past, userProfileImg, userId);
            }
        }
    })
}


function addHTML(id, userName, userEmail, comment, time_past, userProfileImg, userId) {
    let tempHtml = `
        <div class="box comment-list">
            <article class="media">
                <div class="media-left profile-img-area">
                    <a class="image is-64x64" href="/user/${userName}">
                        <img class="is-rounded  profile-img" src='${userProfileImg}'>
                    </a>
                </div>
                <div class="media-content comment-area">
                    <div class="content">
                        <div class="comment-userinfo">                         
                            <strong>${userName}</strong> <small>(${userEmail})</small><small class="comment-time">${time_past}</small>                            
                        </div>
                        <div class = "comment-buttons-area">
                            <a id="${id}-delete" type="button" class="delete-comment" onclick="deleteOne('${id}')"><i class="fa fa-trash" aria-hidden="true"></i></a>     
                            <a id="${id}-edit" type="button" class="edit-comment" onclick="editComment('${id}')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
                            <a id="${id}-update" type="button" class="update-comment" onclick="updateEdit('${id}')"><i class="fa fa-check" aria-hidden="true"></i></a>                                                                                                                         
                        </div>
                    </div>
                    <div class="text-area">
                        <div id="${id}-comment">
                            ${comment}
                        </div>
                        <div id="${id}-editarea" class="edit">
                            <textarea id="${id}-textarea" class="textarea te-edit" name="" id="" cols="30" rows="2"></textarea>
                        </div>
                    </div>
                </div>
            </article>
        </div>`;
    $('#post-box').append(tempHtml);
}

function timePassed(date) {
    let today = new Date()
    // 분을 나타냄
    let time = (today - date) / 1000 / 60

    if (time < 1) {
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

function editComment(id) {
    //comment 값을 text로 textarea에 전달
    let comment = $(`#${id}-comment`).text().trim();
    $(`#${id}-textarea`).val(comment);

    if ($(`#${id}-edit`).css('display') == 'block' && $(`#${id}-comment`).css('display') == 'block') {
        $(`#${id}-edit`).css('display', 'none');
        $(`#${id}-comment`).css('display', 'none');

        if ($(`#${id}-update`).css('display') == 'none' && $(`#${id}-textarea`).css('display') == 'none') {
            $(`#${id}-update`).css('display', 'block');
            $(`#${id}-textarea`).css('display', 'block');
        } else {
            $(`#${id}-update`).css('display', 'none');
            $(`#${id}-textarea`).css('display', 'none');
            $(`#${id}-edit`).css('display', 'block');
            $(`#${id}-comment`).css('display', 'block');
        }
    }
}

function updateEdit(id) {
    let comment = $(`#${id}-textarea`).val().replace(/(?:\r\n|\r|\n)/g, '<br />');
    if (isValidcomment(comment) == false) {
        return;
    }
    let data = {'comment': comment};
    $.ajax({
        type: "PUT",
        url: `/comment/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert(response["msg"]);
            window.location.reload();
        }
    });
}

function deleteOne(id) {
    $.ajax({
        type: "DELETE",
        url: `/comment/${id}`,
        success: function (response) {
            alert(response["msg"]);
            window.location.reload();
        }
    })

}