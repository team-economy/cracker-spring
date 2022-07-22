package com.cracker.user.controller;

import com.cracker.auth.dto.TokenDto;
import com.cracker.common.ResponseDetails;
import com.cracker.user.dto.JoinDto;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/cracker/signup")
    public ResponseEntity<?> join(HttpServletRequest request, HttpServletResponse response, @RequestBody JoinDto requestJoinDTO) {
        ResponseDetails responseDetails;

        Boolean validate = userService.validatePasswordCheck(requestJoinDTO.getPassword());
        System.out.println(validate);
        if(!validate) {
            responseDetails = ResponseDetails.fail(false, "/api/cracker/signup");
            return new ResponseEntity<>(responseDetails, HttpStatus.NOT_ACCEPTABLE);
        }
        TokenDto token = userService.join(response ,requestJoinDTO);

        if (token == null) {
            responseDetails = ResponseDetails.badRequest("회원가입 실패", "/api/cracker/signup");
            return new ResponseEntity<>(responseDetails, HttpStatus.BAD_REQUEST);
        }else {
            responseDetails = ResponseDetails.created(token, "/api/cracker/signup");
            return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
        }
    }

    @PostMapping("/api/cracker/duplicate-email-check")
    public ResponseEntity<?> duplicateEmailCheck(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> requestObject) {
        String msg;
        Boolean duplicate = userService.duplicateEmailCheck(requestObject);
        Boolean validate = userService.validateEmailCheck(requestObject);
        if(duplicate){
            msg = "이미 존재하는 이메일 입니다.";
        }else if(!validate){
            msg = "이메일의 형식을 확인해주세요.";
        }else{
            msg = "사용할 수 있는 이메일입니다.";
        }
        ResponseDetails responseDetails = ResponseDetails.success(msg, "/api/cracker/duplicate-email-check");
        return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
    }

    @PostMapping("/api/cracker/duplicate-nickname-check")
    public ResponseEntity<?> duplicatenicknameCheck(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> requestObject) {
        String msg;
        Boolean duplicate = userService.duplicateNicknameCheck(requestObject);
        Boolean validate = userService.validateNicknameCheck(requestObject);
        if(duplicate){
            msg = "이미 존재하는 별명입니다.";
        }else if(!validate){
            msg = "별명의 형식을 확인해주세요.";
        }else{
            msg = "사용할 수 있는 별명입니다.";
        }
        ResponseDetails responseDetails = ResponseDetails.success(msg, "/api/cracker/duplicate-nickname-check");
        return new ResponseEntity<>(responseDetails, HttpStatus.CREATED);
    }
}
