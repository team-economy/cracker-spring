function resetMarkers() {
    infowindows = []
    markers = []
}

// 맛집 리스트 가져오기
function get_place(flag) {
    $('#place-box').empty();
    $('#place-tabs').addClass("is-active");
    $('#all-place-tabs').removeClass("is-active");
    resetMarkers()
    if (flag == null) {
        $.ajax({
            type: "GET",
            url: '/places',
            data: {},
            success: function (response) {
                console.log(response);
                for (let i = 0; i < response.length; i++) {
                    let place = response[i]
                    let user = checkUsername();
                    let marker = make_marker(place.coordX, place.coordY, place.markerPic)
                    add_info(i, marker, place)
                    make_card(i, place, user, user);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: `/places?userName=${flag}`,
            data: {},
            success: function (response) {
                console.log(response);
                for (let i = 0; i < response.length; i++) {
                    let place = response[i]
                    let user = checkUsername();
                    make_card(i, place, flag, user);
                }
            }
        });
    }
}

function get_all_place() {
    $('#place-box').empty();
    $('#all-place-tabs').addClass("is-active");
    $('#place-tabs').removeClass("is-active");
    resetMarkers()
    $.ajax({
        type: "GET",
        url: `/places/all`,
        success: function (response) {
            console.log(response);
            for (let i = 0; i < response.length; i++) {
                let place = response[i]
                let addr = place.addr;
                let link = place.url;
                let count = place.countPlaces;
                let marker = make_marker(place.coordX, place.coordY, place.markerPic)
                add_info(i, marker, place, link);
                make_all_card(i, place, count);
            }
        }
    });
}

// 맛집 카드 만들기
function make_card(i, place, flag, user) {
    let html_temp_start = `<div class="card" id="card-${i}">
                                <div class="card-body" id="card-body-${i}" style="background-color: #FDF6EC">
                                    <h5 class="card-title"><a href="javascript:click2center(${i})" class="place-title">${place.name}</a></h5>
                                    <p class="card-text">지번 주소 : ${place.addr}</p>
                                    <p class="card-text">도로명 주소 : ${place.addrRoad}</p>
                                    <p class="place-list-button-area">
                                        <button class="button is-success community-btn" onclick="location.href='/community/'+'${place.communityId}'">커뮤니티</button>
                                    `

    let html_temp_my_place = `<button class="button is-danger delete-place-btn" onclick="place_delete_confirm('${place.id}','${place.name}')">삭제</button>`;

    let html_temp_end = `</p>
                    </div>
             </div>`

    let html_temp;

    if (flag == null) {
        html_temp = html_temp_start + html_temp_end;
    } else {
        html_temp = html_temp_start + html_temp_end;
        if (flag == user) {
            html_temp = html_temp_start + html_temp_my_place + html_temp_end;
        }
    }
    $('#place-box').append(html_temp);
}

// 모든 맛집 카드 만들기
function make_all_card(i, place, count) {
    let html_temp = `<div class="card" id="card-${i}">
                        <div class="card-body" id="card-body-${i}" style="background-color: #FDF6EC">
                            
                            <div class="allplce-place-info">
                                <h5 class="card-title"><a href="javascript:click2center(${i})" class="place-title">${place.name}</a></h5>
                                <p class="card-text">카테고리 : ${place.cate}</p>
                                <p class="card-text">지번 주소 : ${place.addr}</p>
                            </div>
                            <div class="add-place-button">
                                <button type="button" class="button add-place-allplace" onclick="add_place(${place.communityId})">
                                    <img src="https://img.icons8.com/sf-regular/48/000000/add.png"/>
                                </button>                        
                            </div>
                           
                            <div class="place-list-button-area">
                             
                                    <span class="place-count float-right">추천수 : ${count}</span>
                                    <button class="button is-success community-btn float-right" onclick="location.href='/community/'+'${place.communityId}'">커뮤니티</button>
                                
                            </div>
                        </div>
                    </div>`;

    $('#place-box').append(html_temp);
}

function checkUsername() {
    let user;
    $.ajax({
        type: "GET",
        async: false,
        url: `/api/cracker/check-username`,
        success: function (response) {
            user = response;
        }
    });
    return user;
}

function place_delete_confirm(place_id, place_name) {
    let html_temp_delete_modal = `
                                <div class="modal" id="confirm-deletion">
                                    <div class="modal-background" onclick='$("#confirm-deletion").removeClass("is-active")'></div>
                                    <div class="modal-content">
                                        <div class="box">
                                            <div class="cofirm msg-section">
                                                <p class="confirm msg"><span><b>${place_name}</b></span>을(를) 삭제하시겠습니까?</p>
                                            </div>
                                            <div class="level-right">
                                                <div class="level-item">
                                                    <button class="button save-button" onclick="delete_place('${place_id}')">예</button>
                                                </div>
                                                <div class="level-item">
                                                    <button class="button confirm cancel-button"
                                                       onclick='$("#confirm-deletion").removeClass("is-active")'>아니오</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>`
    $("#delete-confirm-section").append(html_temp_delete_modal);
    $("#confirm-deletion").addClass("is-active");
}

function searchEvent() {
    if (event.key === "Enter") {
        get_address()
    }
}

// 주소 검색
function get_address() {
    let place_name = $("#input-post").val()
    $("#input-post").val("");
    $("#place_list").empty();
    console.log(place_name)
    $.ajax({
        type: "GET",
        url: `https://dapi.kakao.com/v2/local/search/keyword?query=${place_name}&category_group_code=CS2,FD6,CE7`,
        beforeSend: function (header) {
            header.setRequestHeader("Authorization", 'KakaoAK b2cd5fe8152984068e62cf5b85fbb75a');
        },
        success: function (response) {
            let result = response['documents'];
            console.log(result)

            if (result == "") {
                alert("일치하는 정보가 없습니다.");
            } else {
                $("#modal-post").addClass("is-active");
                for (let i = 0; i < result.length; i++) {
                    let info = result[i];

                    let html_temp = `<div class="form-check place-search-info place-card">
                                            <input class="form-check-input" type="radio" name="place" id="place${i}"
                                            value="${info['place_name']},${info['address_name']},${info['road_address_name']},${info['x']},${info['y']},${info['phone']},${info['category_name']},${info['id']}">
                                            <label class="form-check-label" for="${info['place_name']}" id="label">
                                                <p id="place_name"><b>${info['place_name']}</b></a>
                                                <p>${info['category_name']}</p>
                                                <p>${info['address_name']} | ${info['road_address_name']}</p>
                                            </label>
                                      </div>`
                    $("#place_list").append(html_temp);
                }
            }
        },
        error: function (response) {
            alert("검색어를 입력하세요");
        }
    });
}

// 맞집 저장
function save_place() {
    let radio_button = $('input[name="place"]:checked').val();
    let place = radio_button.split(',')[0];
    let addr = radio_button.split(',')[1];
    let addr_road = radio_button.split(',')[2];

    let x = radio_button.split(',')[3];
    let y = radio_button.split(',')[4];

    let phone = radio_button.split(',')[5];
    let category = radio_button.split(',')[6];

    let url = radio_button.split(',')[7];

    let param = {
        name: place,
        addr: addr,
        addrRoad: addr_road,
        coordX: x,
        coordY: y,
        phoneNum: phone,
        cate: category,
        url: url
    }

    $.ajax({
        type: "POST",
        url: `/places/create`,
        data: JSON.stringify(param),
        contentType: "application/json",
        success: function (response) {
            if (response["msg"] == "저장 완료!!") {
                alert(response["msg"])
                let id = response.id;
                $("#modal-post").removeClass("is-active")
                window.location.href = "/community/" + id;
            } else {
                alert(response["msg"])
                $("#modal-post").removeClass("is-active")
            }
        },
        error: function (response) {
            alert("GUEST 유저입니다.")
            $("#modal-post").removeClass("is-active")
        }
    })
}

// 맛집 삭제
function delete_place(id) {
    $.ajax({
        type: "DELETE",
        url: `/places/${id}`,
        data: {},
        success: function (response) {

            alert(response["msg"]);
            window.location.reload();

        }

    })
}


function add_place(communityId) {

    let community_id = communityId;

    $.ajax({
        type: "POST",
        url: `/places/add/${community_id}`,
        success: function (response) {
            if (response["msg"] == "저장 완료!!") {
                alert(response["msg"])
                let id = response.id;
                $("#modal-post").removeClass("is-active")
                window.location.href = "/community/" + id;
            } else {
                alert(response["msg"])
                $("#modal-post").removeClass("is-active")
            }
        },
        error: function (response) {
            alert("GUEST 유저입니다.")
            $("#modal-post").removeClass("is-active")
        }
    })
}
