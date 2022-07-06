package com.cracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UrlController {
    @RequestMapping("/community")
    public String community(){
        return "community";
    }
}
