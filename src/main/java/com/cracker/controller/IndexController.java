package com.cracker.controller;

import com.cracker.domain.Place;
import com.cracker.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PlaceService placeService;

    @GetMapping("/")
    public String home(){

        return "home";
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
