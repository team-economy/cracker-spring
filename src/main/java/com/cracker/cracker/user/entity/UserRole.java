package com.cracker.cracker.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("USER", "일반 회원 권한"),
    ADMIN("ADMIN", "관리자 권한");

    private final String code;
    private final String displayName;

    public static UserRole of(String code) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(USER);
    }
}
