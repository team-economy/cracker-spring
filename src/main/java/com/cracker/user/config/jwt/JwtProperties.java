package com.cracker.user.config.jwt;

public interface JwtProperties {
    // 서버만 알고 있는 비밀값
    String SECRET = "cracker";
    int EXPIRATION_TIME = 86400000; // 1일 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
