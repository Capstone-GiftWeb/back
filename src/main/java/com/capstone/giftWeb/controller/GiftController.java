package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.dto.GiftsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GiftController {

    @Autowired
    GiftService giftService;

    @GetMapping("/gifts/all")
    public GiftsDTO makeGifts(){
        List<String> gifts=giftService.makeAllGifts();
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);
        return giftsDTO;
    }

    @GetMapping("/gifts")
    public GiftsDTO makeCategoryGifts(@RequestParam("category")String category){
        List<String> gifts=giftService.makeCategoryGifts(category);
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);
        return giftsDTO;
    }

    @GetMapping("/product/{number}")
    public void giftKakao(@PathVariable("number") String number, HttpServletResponse response) throws IOException {

        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

    @GetMapping("gifts/review")
    public GiftsDTO makeReivewGift(@RequestParam(value = "displayTag")String displayTag,@RequestParam(value = "priceRange")String priceRange){

        List<String> gifts = giftService.makeReviewGifts(displayTag, priceRange);
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(gifts);

        return giftsDTO;
    }

}
