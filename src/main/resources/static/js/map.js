let y_cen = 37.4981125   // lat
let x_cen = 127.0379399  // long
let map;
let markers = [];
let infowindows = [];
// 지도 출력
$(document).ready(function () {
    $("#user_id").hide();
    // 초기 화면 전체 목록
    get_all_place();
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(y_cen, x_cen),
        zoom: 12,
        zoomControl: true,
        zoomControlOptions: {
            style: naver.maps.ZoomControlStyle.SMALL,
            position: naver.maps.Position.TOP_RIGHT
        }
    });
})


// 마커 찍기
function make_marker(coordX, coordY, user_marker) {
    console.log(coordX)
    console.log(coordY)
    console.log(user_marker)
    var HOME_PATH = window.HOME_PATH || '.';
    let marker_icon = user_marker
    let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(coordY, coordX),
        map: map,
        icon :user_marker
    });
    markers.push(marker);
    return marker
}

// 마커 클릭 시 정보 출력
function add_info(i, marker, place, link) {
    let html_temp = `<div class="iw-inner">
                                    <h3 class = "place-name"><a href="https://place.map.kakao.com/${link}" target="_blank">${place.name}</a></h3>
                                    <p class="card-text"><b style="color:#8d8b8b;">지번 주소</b><br> <i>${place.addr}</i></p>
                                    <p class="card-text"><b style="color:#8d8b8b;">도로명 주소</b><br><i>${place.addrRoad}</i></p>
                                    <p class="card-text"><b style="color:#8d8b8b;">전화번호</b><br><span>${place.phoneNum}</span></p>
                    </div>`;
    let infowindow = new naver.maps.InfoWindow({
        content: html_temp,
        maxWidth: 300,
        backgroundColor: "#fff",
        borderColor: "#888",
        borderWidth: 1,
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
            $("#place-box").animate({
                scrollTop: $("#place-box").get(0).scrollTop + $(`#card-${i}`).position().top
            }, 1000);
        }
    });
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
