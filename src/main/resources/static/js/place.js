// 맛집 리스트 가져오기
function get_place(flag) {
    $('#place-box').empty();
    markers = []
    infowindows = []
    if (flag == null) {
        $.ajax({
            type: "GET",
            url: '/places',
            data: {},
            success: function (response) {
                console.log(response);
                for (let i = 0; i < response.length; i++) {
                    let place = response[i]
                    let marker = make_marker(place.coordX, place.coordY, place.markerPic)
                    add_info(i, marker, place)
                    make_card(i, place);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: `/places?userMail=${flag}`,
            data: {},
            success: function (response) {
                console.log(response);
                for (let i = 0; i < response.length; i++) {
                    let place = response[i]
                    make_card(i, place);
                }
            }
        });
    }
}

// 맛집 카드 만들기
function make_card(i, place) {
    let html_temp = `<div class="card" id="card-${i}">
                                <div class="card-body" id="card-body-${i}" style="background-color: #FDF6EC">
                                    <h5 class="card-title"><a href="javascript:click2center(${i})" class="place-title">${place.name}</a></h5>
                                    <p class="card-text">지번 주소 : ${place.addr}</p>
                                    <p class="card-text">도로명 주소 : ${place.addrRoad}</p>
                                    <p class="place-list-button-area">
                                        <button class="button is-success community-btn" onclick="location.href='/community/'+'${place.communityId}'">커뮤니티</button>
                                        <button class="button is-danger delete-place-btn" onclick="delete_place('${place.id}')">삭제</button>
                                    </p>
                                </div>
                            </div>`

    $('#place-box').append(html_temp);
}

// 주소 검색
function get_address() {
    let place_name = $("#input-post").val()
    $("#input-post").val("");
    $("#place_list").empty();

    $.ajax({
        type: "GET",
        url: `https://dapi.kakao.com/v2/local/search/keyword?query=${place_name}`,
        beforeSend: function (header) {
            header.setRequestHeader("Authorization", 'KakaoAK b2cd5fe8152984068e62cf5b85fbb75a');
        },
        success: function (response) {

            let result = response['documents'];

            if (result == "") {
                alert("일치하는 정보가 없습니다.");
            } else {
                $("#modal-post").addClass("is-active");
                for (let i = 0; i < result.length; i++) {
                    let info = result[i];

                    let html_temp = `<div class="form-check place-search-info place-card">
                                            <input class="form-check-input" type="radio" name="place" id="place${i}"
                                            value="${info['place_name']},${info['address_name']},${info['road_address_name']},${info['x']},${info['y']},${info['phone']},${info['category_name']}">
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

    let param = {
        name: place,
        addr: addr,
        addrRoad: addr_road,
        coordX: x,
        coordY: y,
        phoneNum: phone,
        cate: category
    }
    console.log(place)
    console.log(addr)
    console.log(addr_road)

    $.ajax({
        type: "POST",
        url: `/places/create`,
        data: JSON.stringify(param),
        contentType: "application/json",
        success: function (response) {
            if (response["msg"] == "저장 완료!!") {
                alert(response["msg"])
                $("#modal-post").removeClass("is-active")
                window.location.reload()
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