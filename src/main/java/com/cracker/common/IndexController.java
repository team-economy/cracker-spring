package com.cracker.common;

import com.cracker.auth.service.AuthService;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.community.entity.Community;
import com.cracker.community.service.CommunityService;
import com.cracker.user.entity.Users;
import com.cracker.user.service.UserService;
import com.cracker.place.domain.Place;
import com.cracker.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final UserService userService;

    private final CommunityService communityService;

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
        Community community = communityService.communitySearch(id);
        model.addAttribute("communityInfo", community);
        return "community";
    }

    //user page 연결
    @GetMapping("/user/{id}")
    public String user(@PathVariable Long id, @CookieValue(required = false, name = "refresh_token") String token, Model model)
    {
        if (token != null) {
            String email = authTokenProvider.getEmailByToken(token);
            Optional<Users> user = authService.getUserByEmail(email);
            Users userInfo = userService.userSearch(id);
            model.addAttribute("user", user);
            model.addAttribute("userInfo", userInfo);
        }
        return "user";

    }

    @GetMapping("/manage")
    public String admin(){

        return "admin";
    }
}
