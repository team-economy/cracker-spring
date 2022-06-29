let y_cen = 37.4981125   // lat
let x_cen = 127.0379399  // long
let map;
let markers = [];
let infowindows = [];
// 지도 출력
$(document).ready(function () {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(y_cen, x_cen),
        zoom: 12,
        zoomControl: true,
        zoomControlOptions: {
            style: naver.maps.ZoomControlStyle.SMALL,
            position: naver.maps.Position.TOP_RIGHT
        }
    });
    get_place()
})

// 맛집 리스트 가져오기
function get_place() {
    $('#matjip-box').empty();
    markers = []
    infowindows = []
    $.ajax({
        type: "GET",
        url: `{{ 맛집리스트 가져오기 }}`,
        data: {},
        success: function (response) {
            let matjips = response["matjip_list"]
            console.log(matjips.length)
            for (let i = 0; i < matjips.length; i++) {
                let matjip = matjips[i]
                make_card(i, matjip)
                let marker = make_marker(matjip, matjip["marker_pic_real"])
                add_info(i, marker, matjip)
            }
        }
    });
}

// 마커 찍기
function make_marker(matjip, user_marker) {
    var HOME_PATH = window.HOME_PATH || '.';
    let marker_icon = user_marker
    let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(matjip["y"], matjip["x"]),
        map: map,
        icon: HOME_PATH+'/static/'+marker_icon
    });
    markers.push(marker);
    return marker
}

// 마커 클릭 시 정보 출력
function add_info(i, marker, matjip) {
    let html_temp = `<div class="iw-inner">
                                    <h5><b>${matjip['matjip_name']}</b></h5>
                                    <p class="card-text">지번 주소 : <i>${matjip['matjip_address']}</i></p>
                                    <p class="card-text">도로명 주소 : <i>${matjip['matjip_road_address']}</i></p>
                                    <p class="card-text">전화번호 : <span class="place-phone">${matjip['phone']}</span></p>
                                    </div>`;
    let infowindow = new naver.maps.InfoWindow({
        content: html_temp,
        maxWidth: 200,
        backgroundColor: "#fff",
        borderColor: "#888",
        borderWidth: 2,
        anchorSize: new naver.maps.Size(15, 15),
        anchorSkew: true,
        anchorColor: "#fff",
        pixelOffset: new naver.maps.Point(10, -10)
    });
    infowindows.push(infowindow)
    naver.maps.Event.addListener(marker, "click", function (e) {
        console.log("clicked", infowindows.length)
        if (infowindow.getMap()) {
            infowindow.close();
        } else {
            infowindow.open(map, marker);
            map.setCenter(infowindow.position)
            $("#matjip-box").animate({
                scrollTop: $("#matjip-box").get(0).scrollTop + $(`#card-${i}`).position().top
            }, 500);
        }
    });
}

// 맛집 카드 만들기
function make_card(i, matjip) {
    let place_addr = matjip["matjip_address"]
    let html_temp = `<div class="card" id="card-${i}">
                                <div class="card-body" style="background-color: #FDF6EC">
                                    <h5 class="card-title"><a href="javascript:click2center(${i})" class="matjip-title">${matjip['matjip_name']}</a></h5>
                                    <p class="card-text">지번 주소 : ${matjip['matjip_address']}</p>
                                    <p class="card-text">도로명 주소 : ${matjip['matjip_road_address']}</p>
                                    <p class="community-delete">
                                    <button class="button is-success" style="background-color: #A0BCC2; font-family: 'Gowun Batang', serif" onclick="location.href='/community/${matjip['matjip_name']}'">커뮤니티
                                    </button>&nbsp&nbsp&nbsp<button class="button is-danger" style="background-color: #ECA6A6; font-family: 'Gowun Batang', serif"" onclick="delete_place('${place_addr}')">삭제</button>
                                    </p>
                                </div>
                            </div>`

    $('#matjip-box').append(html_temp);
}

// 마커 클릭시 화면 중앙으로 이동
function click2center(i) {
    let marker = markers[i]
    let infowindow = infowindows[i]
    if (infowindow.getMap()) {
        infowindow.close();
    } else {
        infowindow.open(map, marker);
        map.setCenter(infowindow.position)
    }
}

// 주소 검색
function get_address() {
    let matjip_name = $("#input-post").val()
    $("#input-post").val("");
    $("#place_list").empty();

    $.ajax({
        type: "GET",
        url: `https://dapi.kakao.com/v2/local/search/keyword?query=${matjip_name}`,
        beforeSend: function (header) {
            header.setRequestHeader("Authorization",'KakaoAK b2cd5fe8152984068e62cf5b85fbb75a');
        },
        success: function(response) {

            let result = response['documents'];

            if (result == "") {
                alert("일치하는 정보가 없습니다.");
            } else {
                $("#modal-post").addClass("is-active");
                for(let i = 0; i < result.length; i++) {
                    let info = result[i];

                    let html_temp = `<div class="form-check">
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
        error: function(response) {
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

    console.log(place)
    console.log(addr)
    console.log(addr_road)

    $.ajax({
        type: "POST",
        url: `/place/save`,
        data: {
            place_give: place,
            addr_give: addr,
            addr_road_give: addr_road,
            x_give: x,
            y_give: y,
            phone_give: phone,
            category_give: category
        },
        success: function (response) {
            if (response["msg"] == "저장 완료!!") {
                alert(response["msg"])
                $("#modal-post").removeClass("is-active")
                window.location.reload()
            } else {
                alert(response["msg"])
                $("#modal-post").removeClass("is-active")
            }
        }

    })
}


// 맛집 삭제
function delete_place(addr) {
    $.ajax({
        type: "DELETE",
        url: `/place/delete`,
        data: {
            addr_give: addr
        },
        success: function (response) {
            if (response["msg"] == "삭제 완료!!") {
                alert(response["msg"])
                window.location.reload()
            } else {
                alert(response["msg"])
            }
        }

    })
}