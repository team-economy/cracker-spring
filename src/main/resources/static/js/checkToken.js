$( document ).ajaxStart(
    function checkToken() {
        $.ajax({
            type: "GET",
            url: "/api/cracker/refresh",
            success: function (response) {
            }
        })
        console.log("response")
    }
);

// let token = $.cookie('access_token');
// if (token!=null){
//     $.ajaxSetup({
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader("Content-type", "application/json");
//             xhr.setRequestHeader("Authorization", "Bearer " + token);
//         }
//     });
// }