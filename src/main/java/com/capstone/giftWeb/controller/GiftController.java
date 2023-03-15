package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.dto.GiftsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class GiftController {

    @Autowired
    GiftService giftService;


    @GetMapping("/product/{number}")
    public void giftKakao(@PathVariable("number") String number, HttpServletResponse response) throws IOException {

        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

}
