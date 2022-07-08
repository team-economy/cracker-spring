package com.cracker.cracker.user.controller;

import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.cracker.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DomainController {
    private final AuthTokenProvider authTokenProvider;
    private final AuthService authService;

    @GetMapping("/")
    public String home(@CookieValue(required = false, name = "refresh_token") String token, Model model) {
        if (token != null) {
            String email = authTokenProvider.getEmailByToken(token);
            Optional<Users> user = authService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/community")
    public String community() {
        return "community";
    }
}
