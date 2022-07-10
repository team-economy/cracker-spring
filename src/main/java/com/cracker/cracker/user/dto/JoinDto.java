package com.cracker.cracker.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class JoinDto {
    private final String email;
    private final String password;
    private final String nickname;
    private final String pic;
    private final String marker_pic;
    private final String role;
    private final String adminToken;
}
