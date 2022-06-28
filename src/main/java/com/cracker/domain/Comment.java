package com.cracker.domain;

import com.cracker.dto.CommentCreateRequestDto;
import com.cracker.dto.CommentUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter // get 함수를 일괄적으로 만듬
@NoArgsConstructor // 기본생성자를 대신 생성
@Entity // 테이블임을 나타냄
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String comment;

    @Builder
    public Comment(String username, String comment){
        this.userName = username;
        this.comment = comment;
    }

    public Comment(CommentCreateRequestDto commentCreateRequestDto){
        this.userName = commentCreateRequestDto.getUsername();
        this.comment = commentCreateRequestDto.getComment();
    }
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto){
        this.userName = commentUpdateRequestDto.getUsername();
        this.comment = commentUpdateRequestDto.getComment();
    }
}
