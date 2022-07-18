function refreshToken() {
    $.ajax({
        type: "GET",
        url: "/api/cracker/refresh",
        success: function (response) {
            console.log(response)
        }
    })
}

$( document ).ajaxStart(function() {
    refreshToken();
});

// let token = $.cookie('access_token');
// if (token!=null){
//     $.ajaxSetup({
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader("Content-type", "application/json");
//             xhr.setRequestHeader("Authorization", "Bearer " + token);
//         }
//     });
// }