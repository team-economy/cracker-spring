package com.cracker.place.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminPlaceListRequestDto {
    private String name;

    private String addr;

    @Builder
    public AdminPlaceListRequestDto(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }
}