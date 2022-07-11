package com.cracker.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체생성 체크
public class CommentUpdateRequestDto {
    //private String userName;
    private String comment;

    @Builder
    public CommentUpdateRequestDto(String comment){
        this.comment = comment;
        //this.userName = username;
    }
}
