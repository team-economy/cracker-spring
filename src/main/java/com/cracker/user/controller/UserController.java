package com.cracker.user.controller;

import com.cracker.user.config.auth.PrincipalDetails;
import com.cracker.user.config.jwt.JwtProperties;
import com.cracker.user.dto.SignUpRequestDto;
import com.cracker.user.model.Users;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
// @CrossOrigin  // CORS 허용
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 모든 사람이 접근 가능
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
    // 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
    @GetMapping("/user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return "<h1>user</h1>";
    }

    @GetMapping("/admin/list")
    public List<Users> userslist() {
        return userService.findUsers();
    }

    @DeleteMapping("/user/delete/{id}")
    public Long deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return id;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String join(@RequestBody SignUpRequestDto signUpRequestDto) {
        userService.registerUser(signUpRequestDto);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) throws IOException, ServletException {
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX);
        return "login";
    }
}
