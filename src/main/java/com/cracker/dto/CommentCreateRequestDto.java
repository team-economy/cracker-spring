package com.cracker.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체생성 체크
public class CommentCreateRequestDto {

    private String username;
    private String comment;


    @Builder
    public CommentCreateRequestDto(String username, String comment){
        this.username = username;
        this.comment = comment;
    }

}
