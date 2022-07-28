package com.cracker.place.dto;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceCreateRequestDto {
    String name;
    String addr;
    String addrRoad;
    String coordX;
    String coordY;
    String phoneNum;
    String cate;
    String markerPic;
    String url;

    @Builder
    public PlaceCreateRequestDto(String name, String addr, String addrRoad, String coordX, String coordY, String phoneNum, String cate, String markerPic, String url) {
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.coordX = coordX;
        this.coordY = coordY;
        this.phoneNum = phoneNum;
        this.cate = cate;
        this.markerPic = markerPic;
        this.url = url;
    }
}
