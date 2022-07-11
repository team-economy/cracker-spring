let y_cen = 37.4981125   // lat
let x_cen = 127.0379399  // long
let map;
let markers = [];
let infowindows = [];
// 지도 출력
$(document).ready(function () {
    $("#user_id").empty();
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
                                    <h5><b>${matjip['place']}</b></h5>
                                    <p class="card-text">지번 주소 : <i>${matjip['addr']}</i></p>
                                    <p class="card-text">도로명 주소 : <i>${matjip['addrRoad']}</i></p>
                                    <p class="card-text">전화번호 : <span class="place-phone">${matjip['phoneNum']}</span></p>
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



