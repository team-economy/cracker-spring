package com.cracker.auth.controller;

import com.cracker.auth.dto.LoginDto;
import com.cracker.auth.dto.TokenDto;
import com.cracker.auth.properties.AppProperties;
import com.cracker.auth.service.AuthService;
import com.cracker.auth.util.CookieUtil;
import com.cracker.common.ResponseDetails;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final AppProperties appProperties;

    @PostMapping("/api/cracker/guest")
    public ResponseEntity<?> guest(HttpServletRequest request, HttpServletResponse response) {
        TokenDto token = authService.guest(request, response);
        ResponseDetails responseDetails;
        responseDetails = ResponseDetails.success(token, "/api/cracker/guest");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @PostMapping("/api/cracker/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto requestLoginDTO) {
        log.info(requestLoginDTO.getEmail());
        TokenDto token = authService.login(request, response, requestLoginDTO);
        ResponseDetails responseDetails;
        if (token == null) {
            responseDetails = ResponseDetails.loginFail("로그인 실패", "/api/cracker/login");
            return new ResponseEntity<>(responseDetails, HttpStatus.UNAUTHORIZED);
        }
        responseDetails = ResponseDetails.success(token, "/api/cracker/login");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @DeleteMapping("/api/cracker/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "access_token", appProperties.getCookieDomain());
        CookieUtil.deleteCookie(request, response, "refresh_token", appProperties.getCookieDomain());
    }

    @GetMapping("/api/cracker/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseDetails responseDetails = authService.refreshToken(request, response);
        return new ResponseEntity<>(responseDetails, HttpStatus.valueOf(responseDetails.getHttpStatus()));
    }
}
