package com.cracker.controller;

import com.cracker.cracker.auth.service.AuthService;
import com.cracker.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.cracker.user.entity.Users;
import com.cracker.domain.Place;
import com.cracker.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PlaceService placeService;

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

    //community page 연결
    @GetMapping("/community/{id}")
    public String commnuity(@PathVariable Long id, Model model){
        Place place = placeService.placeSearch(id);
        model.addAttribute("placeInfo", place);
        return "community";
    }

    //user page 연결
    @GetMapping("/user/{id}")
    public String user(@PathVariable Long id, Model model)
    {
//        User user = userService.userSearch(id);
//        model.addAttribute("userInfo", user);
        return "user";

    }
}
