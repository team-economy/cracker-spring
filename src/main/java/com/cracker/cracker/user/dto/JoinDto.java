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
    private final String nickName;
    private final String role;
}
