package com.cracker.common;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.service.AuthService;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.community.entity.Community;
import com.cracker.community.service.CommunityService;
import com.cracker.place.entity.Place;
import com.cracker.place.service.PlaceService;
import com.cracker.user.entity.UserRole;
import com.cracker.user.entity.Users;
import com.cracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    public String home(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        authService.refreshToken(request, response);
        // guest token 사용하지 않을 경우
        if (userPrincipal == null) {
            model.addAttribute("user", null);
        } else {
            String email = userPrincipal.getEmail();
            Users user = authService.findUserByEmail(email);
            if (user == null) {
                model.addAttribute("user", null);
                return "home";
            } else {
                model.addAttribute("user", user);
            }
            if (user.getRole() == UserRole.ADMIN) {
                return "admin";
            }
        }
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
        if(community == null) {
            return "error/500html";
        } else {
            model.addAttribute("communityInfo", community);
            model.addAttribute("userCommu", user);
            return "community";
        }
    }

    //user page 연결
    @GetMapping("/user/{nickname}")
    public String user(@PathVariable String nickname, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        String email = userPrincipal.getEmail();
        Optional<Users> user = authService.getUserByEmail(email);
        Users userInfo = userService.userSearch(nickname);
        if(user.isEmpty()) {
            return "error/500html";
        }else {
            model.addAttribute("user", user);
            model.addAttribute("userInfo", userInfo);
            return "user";
        }
    }

    @GetMapping("/manage")
    public String admin(){

        return "admin";
    }
}
