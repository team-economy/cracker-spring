package com.cracker.user.dto;

import lombok.*;

public class AuthDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDTO {
        private String email;
        private String pw;
    }

    // Refresh Token을 사용하여 새로운 Access Token을 발급받을 때 사용하는 DTO
    @Getter
    @Setter
    public static class GetNewAccessTokenDTO {
        private long refreshIdx;
    }
}