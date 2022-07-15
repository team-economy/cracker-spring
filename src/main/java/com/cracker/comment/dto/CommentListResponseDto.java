package com.cracker.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentListResponseDto {
    private Long id;
    private String userNickname;
    private String userEmail;
    private String comment;

    private LocalDateTime modifiedAt;

    @Builder
    public CommentListResponseDto(Long id, String userNickname, String userEmail, String comment, LocalDateTime modifiedAt) {
        this.id = id;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.comment = comment;
        this.modifiedAt = modifiedAt;
    }
}
