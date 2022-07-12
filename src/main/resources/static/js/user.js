$(document).ready(function () {
    // get_user_place()
    get_place()
})

// 유저 프로필 이미지 변경
function update_profile() {
    let name = $('#input-user_name').val()
    let file = $('#input-pic')[0].files[0]
    let about = $("#textarea-about").val()

    if (name != $('#user_name').text()) {
        if ($("#help-name").hasClass("is-danger")) {
            alert("별명을 다시 확인해주세요.")
            return;
        } else if (!$("#help-name").hasClass("is-success")) {
            alert("별명 중복확인을 해주세요.")
            return;
        }
    }

    let form_data = new FormData()
    form_data.append("file_give", file)
    form_data.append("name_give", name)
    form_data.append("about_give", about)
    console.log(name, file, about, form_data)
    $.ajax({
        type: "POST",
        url: "/user/update_profile",
        data: form_data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            if (response["result"] == "success") {
                alert(response["msg"])
                window.location.reload()

            }
        }
    });
}

// //유저가 저장한 만집 목록
// function get_user_place() {
//     $('#matjip-box').empty();
//     let user_mail = $("#user_mail").text()
//     $.ajax({
//         type: "GET",
//         url: `/user/place?user_mail_give=${user_mail}`,
//         data: {},
//         success: function (response) {
//             let places = response["user_place"]
//             console.log(places.length)
//             for (let i = 0; i < places.length; i++) {
//                 let place = places[i]
//                 make_card(i, place)
//             }
//         }
//     });
//
// }

// 유저 마커 이미지 변경
function update_marker() {
    let marker = $('#input-marker')[0].files[0]
    let form_marker_data = new FormData()
    form_marker_data.append("marker_give", marker)
    $.ajax({
        type: "POST",
        url: "/user/update_marker",
        data: form_marker_data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            if (response["result"] == "success") {
                alert(response["msg"])
                window.location.reload()
            }
        }
    });
}