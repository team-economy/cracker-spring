package com.cracker.auth.util.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {
    @Getter
    private final String token;
    private final Key key;

    // claim 에 들어갈 키 값을 미리 정의
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String AUTHORITIES_KEY = "role";
    public static final String USER_ID = "email";
    public static final String NICK_NAME = "nickName";

    AuthToken(String email, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(email, expiry);
    }

    AuthToken(String email, String nickName, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(email, nickName, role, expiry);
    }
    
    // refresh token 을 만드는데 이용 될 createAuthToken
    private String createAuthToken(String email, Date expiry) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // access token 을 만드는데 이용 될 createAuthToken
    private String createAuthToken(String email, String nickName, String role, Date expiry) {
        Date now = new Date();

        // .claim 을 이용하여 페이로드에 넣을 값들을 구성해준다.
        return Jwts.builder()
                // pk값을 보통 넣고
                .setSubject(email)
                .setIssuedAt(now)
                // 필요한 정보를 추가적으로
                .claim(AUTHORITIES_KEY, role)
//                .claim(USER_ID, email)
                .claim(NICK_NAME, nickName)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    // 토큰 검증
    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    // 토큰 파싱 시 생길 수 있는 에러를 미리 정의
    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // 토큰의 유효 시간을 확인
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}
