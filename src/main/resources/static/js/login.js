/**
 * 회원가입
 */
//회원 가입
function sign_up() {
    let user_mail = $("#input-user_mail").val().trim()
    let user_name = $("#input-user_name").val()
    let user_pw = $("#input-user_pw").val().trim()
    let user_pw2 = $("#input-password2").val().trim()
    let user_role = $("#input-role").val()
    let adminToken = $("#input-adminToken").val()

    requestJoinDTO =
        {
            "email": user_mail,
            "password": user_pw,
            "nickname": user_name,
            "pic" : "",
            "marker_pic" : "",
            "role": user_role,
            "adminToken" : adminToken
        }

    if ($("#help-mail").hasClass("is-danger")) {
        alert("이메일을 다시 확인해주세요.")
        return;
    } else if (!$("#help-mail").hasClass("is-success")) {
        alert("이메일 중복확인을 해주세요.")
        return;
    }

    if ($("#help-name").hasClass("is-danger")) {
        alert("별명을 다시 확인해주세요.")
        return;
    } else if (!$("#help-name").hasClass("is-success")) {
        alert("별명 중복확인을 해주세요.")
        return;
    }

    if (user_pw == "") {
        $("#help-password").text("비밀번호를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-password").focus()
        return;
    } else if (!is_password(user_pw)) {
        $("#help-password").text("비밀번호의 형식을 확인해주세요. 영문과 숫자 필수 포함, 특수문자(!@#$%^&*) 사용가능 8-20자").removeClass("is-safe").addClass("is-danger")
        $("#input-password").focus()
        return
    } else {
        $("#help-password").text("사용할 수 있는 비밀번호입니다.").removeClass("is-danger").addClass("is-success")
    }
    if (user_pw2 == "") {
        $("#help-password2").text("비밀번호를 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-password2").focus()
        return;
    } else if (user_pw2 != user_pw) {
        $("#help-password2").text("비밀번호가 일치하지 않습니다.").removeClass("is-safe").addClass("is-danger")
        $("#input-password2").focus()
        return;
    } else {
        $("#help-password2").text("비밀번호가 일치합니다.").removeClass("is-danger").addClass("is-success")
    }

    $.ajax({
        type: "POST",
        url: '/api/cracker/signup',
        contentType: "application/json",
        data: JSON.stringify(requestJoinDTO),
        success: function (response) {
            alert("회원가입을 축하드립니다!")
            window.location.replace("/login")
        }
    });
}

// 이메일, 이름, 비밀번호 조건 확인
function is_mail(asValue) {
    var regExp = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
    return regExp.test(asValue);
}

function is_name(asValue) {
    var regExp = /^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$/;
    return regExp.test(asValue);
}

function is_password(asValue) {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$/;
    return regExp.test(asValue);
}

//회원가입 버튼 클릭
function toggle_sign_up() {
    $("#sign-up-name").toggleClass("is-hidden")
    $("#sign-up-box").toggleClass("is-hidden")
    $("#div-sign-in-or-up").toggleClass("is-hidden")
    $("#btn-check-dup-email").toggleClass("is-hidden")
    $("#btn-check-dup-username").toggleClass("is-hidden")
    $("#help-mail").toggleClass("is-hidden")
    $("#help-name").toggleClass("is-hidden")
    $("#help-password").toggleClass("is-hidden")
    $("#help-password2").toggleClass("is-hidden")
}

//관리자 버튼 클릭
function admin_sign_up() {
    $("#admin-sign-up-box").toggleClass("is-hidden")
}

//이메일 중복 확인
function check_email_dup() {
    let user_mail = $("#input-user_mail").val().trim()
    let requestObject = {
        "email": user_mail
    }
    console.log(requestObject.email)
    if (user_mail == "") {
        $("#help-mail").text("이메일을 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-user_mail").focus()
        return;
    }
    if (!is_mail(user_mail)) {
        $("#help-mail").text("이메일의 형식을 확인해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-user_mail").focus()
        return;
    }

    $("#help-mail").addClass("is-loading")
    $.ajax({
        type: "POST",
        url: '/api/cracker/duplicate-email-check',
        contentType: "application/json",
        data: JSON.stringify(requestObject),
        success: function (response) {
            if (response["data"] === true) {
                $("#help-mail").text("이미 존재하는 이메일입니다.").removeClass("is-safe").addClass("is-danger")
                $("#input-user_mail").focus()
            } else {
                $("#help-mail").text("사용할 수 있는 이메일입니다.").removeClass("is-danger").addClass("is-success")
            }
            $("#help-mail").removeClass("is-loading")

        }
    });
}

//유저이름 중복 확인
function check_user_dup() {
    let user_name = $("#input-user_name").val().toString().trim()
    let requestObject = {
        "nickname": user_name
    }
    console.log(requestObject)
    if (user_name == "") {
        $("#help-name").text("별명을 입력해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-user_name").focus()
        return;
    }
    if (!is_name(user_name)) {
        $("#help-name").text("별명의 형식을 확인해주세요.").removeClass("is-safe").addClass("is-danger")
        $("#input-user_name").focus()
        return;
    }

    $("#help-name").addClass("is-loading")
    $.ajax({
        type: "POST",
        url: '/api/cracker/duplicate-nickname-check',
        contentType: "application/json",
        data: JSON.stringify(requestObject),
        success: function (response) {
            if (response["data"] === true) {
                $("#help-name").text("이미 존재하는 별명입니다.").removeClass("is-safe").addClass("is-danger")
                $("#input-user_name").focus()
            } else {
                $("#help-name").text("사용할 수 있는 별명입니다.").removeClass("is-danger").addClass("is-success")
            }
            $("#help-name").removeClass("is-loading")
        }
    });
}

/**
 * 로그인
 */
function sign_in() {
    let user_mail = $("#input-user_mail").val()
    let user_pw = $("#input-user_pw").val()

    let loginDTO =
        {
            "email" : user_mail,
            "password" : user_pw
        }

    if (user_mail == "") {
        $("#help-mail-login").text("이메일을 입력해주세요.")
        $("#input-user_mail").focus()
        return;
    } else {
        $("#help-mail-login").text("")
    }

    if (user_pw == "") {
        $("#help-password-login").text("비밀번호를 입력해주세요.")
        $("#input-user_pw").focus()
        return;
    } else {
        $("#help-password-login").text("")
    }
    $.ajax({
        type: "POST",
        url: '/api/cracker/login',
        contentType: "application/json",
        data: JSON.stringify(loginDTO),
        success: function (response) {
            console.log(response)
            if (response['httpStatus'] === 200) {
                window.location.replace("/")
                // 함수를 만들어서 요청을 할 때 헤더에 쿠키 토큰을 넣
                alert('로그인 성공!')
            } else {
                alert(response['msg'])
            }
        },
        error: function (response) {
            alert("이메일 혹은 비밀번호가 틀렸습니다.")
            window.location.reload()
        }
    });
}

/**
 * 게스트
 */
function guest() {
    $.ajax({
        type: "POST",
        url: '/api/cracker/guest',
        success: function (response) {
            if (response['httpStatus'] === 200) {
                window.location.replace("/")
                alert('게스트입니다.')
            } else {
                alert(response['msg'])
            }
        }
    });
}