package com.cracker.common;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.service.AuthService;
import com.cracker.domain.Place;
import com.cracker.service.PlaceService;
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

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PlaceService placeService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
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
