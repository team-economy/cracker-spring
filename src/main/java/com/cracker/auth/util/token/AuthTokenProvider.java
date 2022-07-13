package com.cracker.auth.util.token;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.exception.ErrorCode;
import com.cracker.exception.TokenValidFailedException;
import com.cracker.user.entity.UserRole;
import io.jsonwebtoken.Claims;
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

    public AuthToken createAuthToken(String email, Date expiry) {
        return new AuthToken(email, expiry, key);
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
            UserRole role = UserRole.valueOf(claims.get(AuthToken.AUTHORITIES_KEY).toString());
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AuthToken.AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());

            // 주의요망
            UserPrincipal userPrincipal = new UserPrincipal(role, claims.getSubject());
            return new UsernamePasswordAuthenticationToken(userPrincipal, authToken, authorities);
        } else {
            throw new TokenValidFailedException(ErrorCode.UNAUTHORIZED, "authToken not validate");
        }
    }
}
