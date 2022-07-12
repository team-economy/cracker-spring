package com.cracker.comment.domain;

import com.cracker.comment.dto.CommentCreateRequestDto;
import com.cracker.comment.dto.CommentUpdateRequestDto;
import com.cracker.place.domain.Place;
import com.cracker.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter // get 함수를 일괄적으로 만듬
@NoArgsConstructor // 기본생성자를 대신 생성
@Entity // 테이블임을 나타냄
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="place_id", nullable = false)
    private Place place;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @Builder
    public Comment(String comment){
        this.comment = comment;
    }

    public Comment(CommentCreateRequestDto commentCreateRequestDto){
        this.comment = commentCreateRequestDto.getComment();
    }
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto){
        this.comment = commentUpdateRequestDto.getComment();
    }

    public void placeUserComment(Place place, Users users){
        this.place = place;
        this.users = users;
        place.getComments().add(this);
        users.getComments().add(this);
    }
}
