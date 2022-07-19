package com.cracker.auth.filter;

import com.cracker.auth.util.token.AuthToken;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.auth.util.CookieUtil;
import com.cracker.exception.TokenValidFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());
        log.info("commit");
//        String tokenStr = HeaderUtil.getAccessToken(request);
//        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        Cookie tokenStr = CookieUtil.getCookie(request, "access_token").orElse(null);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr != null ? tokenStr.getValue() : null);
        try {
            if (tokenStr != null && token.validate()) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenValidFailedException e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
