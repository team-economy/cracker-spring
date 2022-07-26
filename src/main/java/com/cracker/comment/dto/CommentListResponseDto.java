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
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String userProfileImg;

    private Long userId;

    private boolean modified;

    @Builder
    public CommentListResponseDto(Long id, String userNickname, String comment, LocalDateTime modifiedAt,LocalDateTime createdAt, String userProfileImg, Long userId, boolean modified) {
        this.id = id;
        this.userNickname = userNickname;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.userProfileImg = userProfileImg;
        this.userId = userId;
        this.modified = modified;
    }
}
