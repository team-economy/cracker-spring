/**
 * 로그아웃
 */
function sign_out() {
    $.removeCookie('access_token', {path: '/'});
    $.removeCookie('refresh_token', {path: '/'});
    alert('로그아웃!')
    window.location.href = "/login"
}