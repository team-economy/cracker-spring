/**
 * 로그아웃
 */
function sign_out() {
    $.removeCookie('access_token', {path: '/'});
    $.removeCookie('refresh_token', {path: '/'});
    $.ajax({
        type: "DELETE",
        url: `/api/cracker/logout`,
        global: false,
        success: function (response) {
            alert('로그아웃!');
            window.location.href = "/login"
        }
    });
}