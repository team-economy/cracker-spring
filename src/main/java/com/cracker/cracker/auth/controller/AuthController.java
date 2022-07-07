package com.cracker.cracker.auth.controller;

import com.cracker.cracker.auth.dto.LoginDto;
import com.cracker.cracker.auth.dto.TokenDto;
import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.common.ResponseDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto requestLoginDTO) {
        System.out.println("게이트웨이를 통해 들어왔습니다! :)");
        TokenDto token = authService.login(request, response, requestLoginDTO);
        ResponseDetails responseDetails;
        if (token == null) {
            responseDetails = ResponseDetails.loginFail("로그인 실패", "/api/auth/login");
            return new ResponseEntity<>(responseDetails, HttpStatus.UNAUTHORIZED);
        }
        responseDetails = ResponseDetails.success(token, "/api/auth/login");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseDetails responseDetails = authService.refreshToken(request, response);
        return new ResponseEntity<>(responseDetails, HttpStatus.valueOf(responseDetails.getHttpStatus()));
    }
}
