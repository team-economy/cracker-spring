package com.cracker.place.dto;

import com.cracker.user.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCommentListRequestDto {
    private Users users;

    private String comment;

    @Builder
    public AdminCommentListRequestDto(Users users, String comment) {
        this.users = users;
        this.comment = comment;
    }
}

