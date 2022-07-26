/**
 * 로그아웃
 */
function sign_out() {
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