package com.capstone.giftWeb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/api")
public class SwaggerTestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
