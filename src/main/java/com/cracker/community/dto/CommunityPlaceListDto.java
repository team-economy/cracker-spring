package com.cracker.community.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPlaceListDto {
    private Long id;
    private String name;
    private String addr;
    private String addrRoad;
    private String phoneNum;
    private String cate;

    @Builder
    public CommunityPlaceListDto(Long id, String name, String addr, String addrRoad,
                                 String phoneNum, String cate) {
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.addrRoad = addrRoad;
        this.phoneNum = phoneNum;
        this.cate = cate;
    }
}
