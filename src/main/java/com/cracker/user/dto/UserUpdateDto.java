package com.cracker.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateDto {
    private String name;
    private String pw;
    private String pic_real;
    private String marker_pic_real;
    private String status;

    @Builder
    public UserUpdateDto(String name,
                         String pw,
                         String pic_real,
                         String marker_pic_real,
                         String status) {
        this.name = name;
        this.pw = pw;
        this.pic_real = pic_real;
        this.marker_pic_real = marker_pic_real;
        this.status = status;
    }
}
