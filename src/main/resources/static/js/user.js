$(document).ready(function () {
    let userMail = $('#user_mail').text();
    get_place(userMail);
})

var fileTarget = $('.filewrap .uploadBtn');
fileTarget.on('change', function(){ // 값이 변경되면
    if(window.FileReader){ // modern browser
        var filename = $(this)[0].files[0].name;
    }
    else { // old IE
        var filename = this.files[0].name; // 파일명만 추출
    }  // 추출한 파일명 삽입
    $(this).siblings('.fileName').val(filename);

});
// 유저 프로필 이미지 변경
function update_profile(id) {
    let name = $('#input-user_name').val()
    let file = $('#input-pic')[0].files[0]
    let about = $("#textarea-about").val()

    if (name =='') {
        name = $('#user_name').text()
    }

    if (about =='') {
        about = $('#user_about').text()
    }

    if (name !='' && name != $('#user_name').text()) {
        if ($("#help-name").hasClass("is-danger")) {
            alert("별명을 다시 확인해주세요.")
            return;
        } else if (!$("#help-name").hasClass("is-success")) {
            alert("별명 중복확인을 해주세요.")
            return;
        }
    }

    let form_data = new FormData()
    form_data.append("file", file)
    form_data.append("nickname", name)
    form_data.append("statusMessage", about)
    console.log(file, name, about, form_data)
    $.ajax({
        type: "POST",
        url: `/user/update_profile/${id}`,
        data: form_data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            if (response["msg"] == "변경 완료!!") {
                alert(response["msg"])
                $("#modal-edit").removeClass("is-active")
                window.location.replace("/user/" + name)
            } else {
                alert(response["msg"])
                $("#modal-edit").removeClass("is-active")
            }
        }
    });
}

// 유저 마커 이미지 변경
function update_marker(id) {
    let marker = $('#input-marker')[0].files[0]
    let form_marker_data = new FormData()
    form_marker_data.append("file", marker)
    $.ajax({
        type: "POST",
        url: `/user/update_marker/${id}`,
        data: form_marker_data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            if (response["msg"] == "변경 완료!!") {
                alert(response["msg"])
                $("#modal-edit_marker").removeClass("is-active")
                window.location.reload()
            } else {
                alert(response["msg"])
                $("#modal-edit_marker").removeClass("is-active")
            }
        }
    });
}