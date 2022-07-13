package com.cracker.cracker.auth.controller;

import com.cracker.cracker.auth.dto.LoginDto;
import com.cracker.cracker.auth.dto.TokenDto;
import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.common.ResponseDetails;
import com.cracker.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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

//    @GetMapping("/api/kakao/login")
//    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response){
//        // authorizedCode: 카카오 서버로부터 받은 인가 코드
//        TokenDto token = userService.kakao(code, response);
//        ResponseDetails responseDetails;
//        if (token == null) {
//            responseDetails = ResponseDetails.fail("토큰 발급에 실패했습니다.", "/api/kakao/login");
//            return new ResponseEntity<>(responseDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        responseDetails = ResponseDetails.success(token, "/api/kakao/login");
//        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
//    }
}
