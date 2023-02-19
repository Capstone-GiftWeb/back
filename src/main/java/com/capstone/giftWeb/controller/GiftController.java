package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.dto.GiftsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GiftController {

    @Autowired
    GiftService giftService;

    @GetMapping("/gifts")
    @ResponseBody
    public GiftsDTO makeGifts(){
        List<String> gifts=giftService.makeGifts();
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);
        return giftsDTO;
    }

    @GetMapping("/product/{number}")
    public String giftKakao(@PathVariable("number") String number){

        return "redirect:https://gift.kakao.com/product/"+number;
    }

}
