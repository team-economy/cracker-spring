package com.cracker.cracker.auth.util.token;

import com.cracker.cracker.auth.security.UserPrincipal;
import com.cracker.cracker.exception.ErrorCode;
import com.cracker.cracker.exception.TokenValidFailedException;
import com.cracker.cracker.user.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(Date expiry) {
        return new AuthToken(expiry, key);
    }

    public AuthToken createAuthToken(String email, String nickName, String role, Date expiry) {
        return new AuthToken(email, nickName, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) throws TokenValidFailedException {

        if (authToken.validate()) {

            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AuthToken.AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());

            UserPrincipal userPrincipal = new UserPrincipal((String) claims.get(AuthToken.USER_ID));
            return new UsernamePasswordAuthenticationToken(userPrincipal, authToken, authorities);
        } else {
            throw new TokenValidFailedException(ErrorCode.UNAUTHORIZED, "authToken not validate");
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
