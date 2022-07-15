package com.cracker.common;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.service.AuthService;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.community.entity.Community;
import com.cracker.community.service.CommunityService;
import com.cracker.place.service.PlaceService;
import com.cracker.user.entity.Users;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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
    public String home(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        String email = userPrincipal.getEmail();
        Users user = authService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/api/kakao/login")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response){
        userService.kakao(code, response);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //community page 연결
    @GetMapping("/community/{id}")
    public String commnuity(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model){
        String email = userPrincipal.getEmail();
        Optional<Users> user = authService.getUserByEmail(email);
        Community community = communityService.communitySearch(id);
        model.addAttribute("communityInfo", community);
        model.addAttribute("userCommu", user);
        return "community";
    }

    //user page 연결
    @GetMapping("/user/{id}")
    public String user(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model)
    {
        String email = userPrincipal.getEmail();
        Optional<Users> user = authService.getUserByEmail(email);
        Users userInfo = userService.userSearch(id);
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);

        return "user";

    }

    @GetMapping("/manage")
    public String admin(){

        return "admin";
    }
}
