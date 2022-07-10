let token = $.cookie('accessToken');
if (token!=null){
    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    });
}