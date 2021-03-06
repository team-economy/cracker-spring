package com.cracker.auth.service;

import com.cracker.auth.properties.AppProperties;
import com.cracker.auth.util.token.AuthToken;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.auth.dto.LoginDto;
import com.cracker.auth.dto.TokenDto;
import com.cracker.auth.util.CookieUtil;
import com.cracker.auth.util.HeaderUtil;
import com.cracker.common.ResponseDetails;
import com.cracker.user.entity.UserRole;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private final static long THREE_DAYS_MSEC = 259200000;

    public Optional<Users> getUserByEmail(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        return user;
    }

    /**
     * guest token 발급
     */
    public AuthToken guestToken() {
        return tokenProvider.createAuthToken();
    }

    /**
     * refresh token 발급 및 users update
     */
    public AuthToken refreshToken(Users user) {
        Date now = new Date();
        long refreshTokenExpiry = appProperties.getRefreshTokenExpiry();
        
        AuthToken refreshToken = tokenProvider.createAuthToken(user.getEmail(), new Date(now.getTime() + refreshTokenExpiry));
        user.updateRefreshToken(refreshToken.getToken());

        return refreshToken;
    }

    /**
     * access token 발급
     */
    public AuthToken AccessToken(Users user) {
        Date now = new Date();
        return tokenProvider.createAuthToken(
                user.getEmail(),
                user.getNickname(),
                user.getRole().getCode(),
                new Date(now.getTime() + appProperties.getTokenExpiry())
        );
    }

    /**
     * guest
     */
    public TokenDto guest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AuthToken guestToken = guestToken();

        guestTokenAddCookie(httpResponse, guestToken.getToken());

        return new TokenDto(guestToken.getToken());
    }

    /**
     * login
     */
    public TokenDto login(HttpServletRequest httpRequest, HttpServletResponse httpResponse, LoginDto requestLoginDTO) {
        Optional<Users> userOptional = userRepository.findByEmail(requestLoginDTO.getEmail());
        if (userOptional.isEmpty()) {
            return null;
        }

        Users user = userOptional.get();

        if (!passwordEncoder.matches(requestLoginDTO.getPassword(), user.getPassword())) {
            return null;
        }

        AuthToken refreshToken = refreshToken(user);
        AuthToken accessToken = AccessToken(user);

        refreshTokenAddCookie(httpResponse, refreshToken.getToken());
        accessTokenAddCookie(httpResponse, accessToken.getToken());

        return new TokenDto(accessToken.getToken());
    }

    /**
     * 헤더에 guest token 추가
     */
    public void guestTokenAddCookie(HttpServletResponse response, String guestToken) {
        long accessTokenExpiry = appProperties.getTokenExpiry();
        int cookieMaxAge = (int) accessTokenExpiry / 60;
        CookieUtil.addCookie(response, AuthToken.ACCESS_TOKEN, guestToken, cookieMaxAge, appProperties.getCookieDomain());
    }

    /**
     * 헤더에 refresh token 추가
     */
    public void refreshTokenAddCookie(HttpServletResponse response, String refreshToken) {
        long refreshTokenExpiry = appProperties.getRefreshTokenExpiry();
        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.addCookie(response, AuthToken.REFRESH_TOKEN, refreshToken, cookieMaxAge, appProperties.getCookieDomain());
    }

    /**
     * 헤더에 access token 추가
     */
    public void accessTokenAddCookie(HttpServletResponse response, String accessToken) {
        long accessTokenExpiry = appProperties.getTokenExpiry();
        int cookieMaxAge = (int) accessTokenExpiry / 60;
        CookieUtil.addCookie(response, AuthToken.ACCESS_TOKEN, accessToken, cookieMaxAge, appProperties.getCookieDomain());
    }

    /**
     * 토큰 재발급
     */
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // refresh token
        String refreshToken = CookieUtil.getCookie(request, AuthToken.REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (!authRefreshToken.validate()) {
            CookieUtil.deleteCookie(request, response, AuthToken.ACCESS_TOKEN, appProperties.getCookieDomain());
            if (refreshToken != null) {
                CookieUtil.deleteCookie(request, response, AuthToken.REFRESH_TOKEN, appProperties.getCookieDomain());
            }
            return null;
        }

        Claims claims = authRefreshToken.getTokenClaims();
        String email = claims.getSubject();

        // userId refresh token 으로 DB 확인
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );

        String userRefreshToken = user.getRefreshToken();
        if (!userRefreshToken.equals(refreshToken)) {
            CookieUtil.deleteCookie(request, response, AuthToken.ACCESS_TOKEN, appProperties.getCookieDomain());
            CookieUtil.deleteCookie(request, response, AuthToken.REFRESH_TOKEN, appProperties.getCookieDomain());
            return null;
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                user.getEmail(),
                user.getNickname(),
                user.getRole().getCode(),
                new Date(now.getTime() + appProperties.getTokenExpiry())
        );

        CookieUtil.deleteCookie(request, response, AuthToken.ACCESS_TOKEN, appProperties.getCookieDomain());
        accessTokenAddCookie(response, newAccessToken.getToken());

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(user.getEmail(), new Date(now.getTime() + refreshTokenExpiry));

            // DB에 refresh 토큰 업데이트
            user.updateRefreshToken(authRefreshToken.getToken());

            CookieUtil.deleteCookie(request, response, AuthToken.REFRESH_TOKEN, appProperties.getCookieDomain());
            refreshTokenAddCookie(response, authRefreshToken.getToken());
        }

        return email;
    }

    public Users findUserByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElse(null);
        return user;
    }
}