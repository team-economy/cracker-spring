package com.cracker.place.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceListRequestDto {
    private Long id;
    private String name;
    private String addr;
    private String addrRoad;
    private String coordX;
    private String coordY;
    private String phoneNum;
    private String cate;
    private Long communityId;

    private String markerPic;

    @Builder
    public PlaceListRequestDto(Long id, String name, String addr, String addrRoad, String coordX,
                               String coordY, String phoneNum, String cate, Long communityId, String markerPic) {
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.coordX = coordX;
        this.coordY = coordY;
        this.phoneNum = phoneNum;
        this.cate = cate;
        this.communityId = communityId;
        this.markerPic = markerPic;
    }

}
