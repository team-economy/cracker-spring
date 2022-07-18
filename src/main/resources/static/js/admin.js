function adminPlaces() {
    $("#adminList").empty();
    $.ajax( {
        type: "GET",
        url: "/admin/places",
        data: {},
        success: function (response) {
            console.log(response)
            for (let i = 0; i < response.length; i++) {
                let places = response[i]
                listPlaces(i, places)
            }
        }
    })
}


function listPlaces(i, places) {
    let place_info = places["matjip_address"]
    console.log(places)
    let html_temp = `<tr id="place-${i}">
                        <td>${places.id}</td>
                        <td>${places.name}</td>
                        <td>${places.addr}</td>
                        <td> <button class="button" onclick="deleteAdminPlace(${places.id})"> DELETE </button> </td>
                    </tr>`
    $('#adminList').append(html_temp);
}
function deleteAdminPlace(id) {
    $.ajax({
        type: "DELETE",
        url: `/manage/places/${id}`,
        data: {},
        success: function (response) {
            if (response["msg"] == "삭제 완료!!") {
                alert(response["msg"])
                window.location.reload()
            } else {
                alert(response["msg"])
            }
            // adminPlaces();
        }
    })
}

// function adminComments() {
//     $.ajax( {
//         type: "GET",
//         url: "/manage/comments",
//         data: {},
//         success: function (response) {
//             for (let i = 0; i < response.length; i++) {
//                 let place = response[i]
//                 listPlaces(i, place)
//             }
//         }
//     })
// }