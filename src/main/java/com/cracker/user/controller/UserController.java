package com.cracker.user.controller;

import com.cracker.user.dto.AuthDto;
import com.cracker.user.dto.TokenDto;
import com.cracker.user.dto.UserRequestDto;
import com.cracker.user.model.Users;
import com.cracker.user.service.UserAuthService;
import com.cracker.user.service.UserService;
import com.cracker.user.utils.HttpStatusChangeInt;
import com.cracker.user.utils.ResponseDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@AllArgsConstructor
// @CrossOrigin  // CORS 허용
public class UserController {
    private final UserService userService;
    private final UserAuthService userAuthService;

    @PostMapping("/api/user/signup")
    public String signup(@RequestBody UserRequestDto requestDto) {
        userService.registerUser(requestDto);
        return "success signup";
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginDTO loginDTO) throws AccessDeniedException {
        TokenDto tokenDto = userAuthService.login(loginDTO);
        int httpStatus = HttpStatusChangeInt.ChangeStatusCode("OK");
        ResponseDetails responseDetails = new ResponseDetails(new Date(), tokenDto, httpStatus, "success");
        return new ResponseEntity<>(responseDetails, HttpStatus.OK);
    }

    @PostMapping("/api/user/checkEmailDup")
    public String checkEmailDup(@RequestParam String email) {
        if (userService.checkEmailDup(email).equals("exists")) {
            return "exists";
        } else {
            return "Email Duplication Checked.";
        }
    }

    @PostMapping("/api/user/checkNicknameDup")
    public String checkNicknameDup(@RequestParam String nickname) {
        if (userService.checkNicknameDup(nickname).equals("exists")) {
            return "exists";
        } else {
            return "Email Duplication Checked.";
        }
    }

//    @GetMapping("api/user/list")
//    public List<Users> findUsers() {
//        return userService.findUsers();
//    }

    @PostMapping("/api/user/logout")
    public ResponseEntity<?> logoutToken(HttpServletRequest request) throws AccessDeniedException {
        userAuthService.logout(request);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }
}