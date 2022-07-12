package com.cracker.cracker.user.controller;

import com.cracker.cracker.auth.dto.TokenDto;
import com.cracker.cracker.common.ResponseDetails;
import com.cracker.cracker.user.dto.JoinDto;
import com.cracker.cracker.user.dto.UpdateUserRequestDto;
import com.cracker.cracker.user.entity.Users;
import com.cracker.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/cracker/signup")
    public ResponseEntity<?> join(HttpServletRequest request, HttpServletResponse response, @RequestBody JoinDto requestJoinDTO) {
        TokenDto token = userService.join(response ,requestJoinDTO);
        ResponseDetails responseDetails;
        if (token == null) {
            responseDetails = ResponseDetails.badRequest("회원가입 실패", "/api/cracker/signup");
            return new ResponseEntity<>(responseDetails, HttpStatus.BAD_REQUEST);
        }
        responseDetails = ResponseDetails.created(token, "/api/cracker/signup");
        return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
    }

    @PostMapping("/api/cracker/duplicate-email-check")
    public ResponseEntity<?> duplicateEmailCheck(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> requestObject) {
        Boolean duplicate = userService.duplicateEmailCheck(requestObject);
        ResponseDetails responseDetails = ResponseDetails.success(duplicate, "/api/cracker/duplicate-email-check");
        return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
    }

    @PostMapping("/api/cracker/duplicate-nickname-check")
    public ResponseEntity<?> duplicatenicknameCheck(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> requestObject) {
        Boolean duplicate = userService.duplicateNicknameCheck(requestObject);
        ResponseDetails responseDetails = ResponseDetails.success(duplicate, "/api/cracker/duplicate-nickname-check");
        return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
    }

//    @PutMapping("/api/cracker/update/{id}")
//    public Long profileUpdate(@PathVariable Long id, @RequestPart MultipartFile multipartFile, @RequestBody UpdateUserRequestDto updateUserRequestDto) throws IOException {
//        userService.updateProfile(id, multipartFile, updateUserRequestDto);
//        return id;
//    }
}
