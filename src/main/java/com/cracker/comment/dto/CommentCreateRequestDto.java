package com.cracker.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체생성 체크
public class CommentCreateRequestDto {

//    private String userName;
    @Length(min=1, max=255)
    private String comment;

    private String communityAddr;


    @Builder
    public CommentCreateRequestDto(String comment){
//        this.userName = username;
        this.comment = comment;
    }

}
