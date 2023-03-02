package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.dto.GiftsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GiftController {

    @Autowired
    GiftService giftService;

    @GetMapping("/gifts/all")
    @ResponseBody
    public GiftsDTO makeGifts(){
        List<String> gifts=giftService.makeAllGifts();
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);
        return giftsDTO;
    }

    @GetMapping("/gifts")
    @ResponseBody
    public GiftsDTO makeCategoryGifts(@RequestParam("category")String category){
        List<String> gifts=giftService.makeCategoryGifts(category);
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);
        return giftsDTO;
    }

    @GetMapping("/product/{number}")
    public String giftKakao(@PathVariable("number") String number){

        return "redirect:https://gift.kakao.com/product/"+number;
    }

}
