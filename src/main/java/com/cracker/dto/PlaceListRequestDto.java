package com.cracker.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceListRequestDto {
    private String name;
    private String addr;
    private String addrRoad;
    private String coordX;
    private String coordY;
    private String phoneNum;
    private String cate;

    @Builder
    public PlaceListRequestDto(String name, String addr, String addrRoad, String coordX,
                               String coordY, String phoneNum, String cate) {
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.coordX = coordX;
        this.coordY = coordY;
        this.phoneNum = phoneNum;
        this.cate = cate;

    }

}