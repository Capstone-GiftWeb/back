package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        String gifts = (String) session.getAttribute("gifts");
        if (gifts != null) {
            model.addAttribute("gifts", session.getAttribute("gifts"));
            System.out.println(gifts);
        } else {
            List<String> list = homeService.makeGifts();
            session.setMaxInactiveInterval(60 * 60);
            session.setAttribute("gifts", list.toString());
            model.addAttribute("gifts", session.getAttribute("gifts"));
        }

        return "home";
    }

}
