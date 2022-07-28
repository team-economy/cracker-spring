$(document).ready(function () {
    // HTML 문서를 로드할 때마다 실행
    getMessages();
})

function isValidcomment(comment) {
    if (comment == "") {
        alert('내용을 입력해주세요');
        return false;
    }
    if (comment.trim().length > 255) {
        alert('255자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function registerComment() {
    if(event.key === "Enter") {
        if(!event.shiftKey) {
            post()
        }
    }
}

function post() {
    // 작성한 메모
    let comment = $('#textarea-post').val();
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
        url: "/comment/create",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('댓글작성 완료');
            window.location.reload();
        },
        error: function (response) {
            alert("로그인해주세요.")
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
                let comment = message['comment'].replace(/(?:\r\n|\r|\n)/g, '<br />');
                let time_comment = new Date(message['modifiedAt'])
                let time_past = timePassed(time_comment)
                let userProfileImg = message['userProfileImg'];
                let userId = message['userId'];
                let isModified = message['modified'];
                let user = checkUser();

                console.log(userId)
                console.log(user)

                addHTML(id, username, comment, time_past, userProfileImg, userId, isModified, user);
            }
        }
    })
}

function checkUser() {
    let user;
    $.ajax({
        type: "GET",
        async: false,
        url: `/api/cracker/check-user`,
        success: function (response) {
            user = response;
        }
    });
    return user;
}

function addHTML(id, userName, comment, time_past, userProfileImg, userId, isModified, user) {
    let tempHtml_start = `
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
                            `;
    let tempHtml_not_modified = `<strong>${userName}</strong><small class="comment-time">${time_past}</small>
                                </div>`;

    let tempHtml_is_modified = `<strong>${userName}</strong><small class="comment-time">${time_past} (수정됨)</small>
                                </div>`;

    let tempHtml_isMe = `<div class = "comment-buttons-area">
                            <a id="${id}-delete" type="button" class="delete-comment" onclick="comment_delete_confirm('${id}')"><i class="fa fa-trash" aria-hidden="true"></i></a>     
                            <a id="${id}-edit" type="button" class="edit-comment" onclick="editComment('${id}')"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
                            <a id="${id}-update" type="button" class="update-comment" onclick="updateEdit('${id}')"><i class="fa fa-check" aria-hidden="true"></i></a>                                                                                                                         
                        </div>`

    let tempHtml_end = `
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

    let tempHtml;

    if (user === userId) {
        if(isModified) {
            tempHtml = tempHtml_start + tempHtml_is_modified + tempHtml_isMe + tempHtml_end;
        } else {
            tempHtml = tempHtml_start + tempHtml_not_modified + tempHtml_isMe + tempHtml_end;
        }
    } else {
        if(isModified) {
            tempHtml = tempHtml_start + tempHtml_is_modified + tempHtml_end;
        } else {
            tempHtml = tempHtml_start + tempHtml_not_modified + tempHtml_end;
        }
    }

    $('#post-box').append(tempHtml);
}

function comment_delete_confirm(comment_id){
    let html_temp_delete_modal = `
                                <div class="modal" id="confirm-deletion">
                                    <div class="modal-background" onclick='$("#confirm-deletion").removeClass("is-active")'></div>
                                    <div class="modal-content">
                                        <div class="box">
                                            <div class="cofirm msg-section">
                                                <p class="confirm msg"><span><b>댓글 삭제</b></span></p>
                                                <p>댓글을 완전히 삭제할까요?</p>
                                            </div>
                                            <div class="level-right">
                                                <div class="level-item">
                                                    <button class="button save-button" onclick="deleteOne('${comment_id}')">예</button>
                                                </div>
                                                <div class="level-item">
                                                    <button class="button confirm cancel-button"
                                                       onclick='$("#confirm-deletion").removeClass("is-active")'>아니오</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>`
    $("#delete-confirm-section").append(html_temp_delete_modal);
    $("#confirm-deletion").addClass("is-active");
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