package com.cracker.cracker.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserRequestDto {
    private final String nickname;
    private final String status;
}
