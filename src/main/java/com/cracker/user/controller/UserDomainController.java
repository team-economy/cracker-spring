package com.cracker.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDomainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/api/user/")
    public String login() {
        return "login";
    }
}
