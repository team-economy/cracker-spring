package com.cracker.cracker.user.controller;

import com.cracker.cracker.auth.security.UserPrincipal;
import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DomainController {
    private final AuthService authService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String email = userPrincipal.getEmail();
        Users user = authService.findUserByEmail(email);
        model.addAttribute("user", user);
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
