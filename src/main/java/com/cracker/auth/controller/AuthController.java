package com.cracker.auth.controller;

import com.cracker.auth.dto.LoginDto;
import com.cracker.auth.dto.TokenDto;
import com.cracker.auth.service.AuthService;
import com.cracker.common.ResponseDetails;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/api/cracker/guest")
    public ResponseEntity<?> guest(HttpServletRequest request, HttpServletResponse response) {
        TokenDto token = authService.guest(request, response);
        ResponseDetails responseDetails;
        responseDetails = ResponseDetails.success(token, "/api/cracker/guest");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @PostMapping("/api/cracker/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto requestLoginDTO) {
        TokenDto token = authService.login(request, response, requestLoginDTO);
        ResponseDetails responseDetails;
        if (token == null) {
            responseDetails = ResponseDetails.loginFail("로그인 실패", "/api/cracker/login");
            return new ResponseEntity<>(responseDetails, HttpStatus.UNAUTHORIZED);
        }
        responseDetails = ResponseDetails.success(token, "/api/cracker/login");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @GetMapping("/api/cracker/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseDetails responseDetails = authService.refreshToken(request, response);
        return new ResponseEntity<>(responseDetails, HttpStatus.valueOf(responseDetails.getHttpStatus()));
    }
}
