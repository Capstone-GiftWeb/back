package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String home(HttpServletResponse response,HttpServletRequest request) {
        int flag=0;  //쿠키에 makeGifts가 없으면 크롤링, 있으면 크롤링X
        Cookie[] cookies = request.getCookies();
        for (Cookie cok : cookies) {
            if(cok.getName().equals("makeGifts"))
                flag=1;
        }
        if(flag==0) {
            List<String> list = homeService.makeGifts();
            Cookie cookie = new Cookie("makeGifts", "yes");
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "home";
    }
}
