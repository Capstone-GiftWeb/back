package com.capstone.giftWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/")
    @ResponseBody
    public String test(){
        return "재발급 성공";
    }
}
