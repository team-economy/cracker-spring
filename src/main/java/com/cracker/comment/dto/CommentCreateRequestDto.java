package com.cracker.comment.dto;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체생성 체크
public class CommentCreateRequestDto {

//    private String userName;
    
    private String comment;

    private String communityAddr;


    @Builder
    public CommentCreateRequestDto(String comment){
//        this.userName = username;
        this.comment = comment;
    }

}
