package com.cracker.place.dto;

import com.cracker.user.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCommentListRequestDto {

    private Long id;
    private String userNickname;

    private String userEmail;

    private String comment;

    private LocalDateTime modifiedAt;

    @Builder
    public AdminCommentListRequestDto(Long id, String userNickname, String userEmail, String comment, LocalDateTime modifiedAt) {
        this.id = id;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.comment = comment;
        this.modifiedAt = modifiedAt;
    }
}

