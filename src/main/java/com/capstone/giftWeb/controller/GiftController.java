package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.dto.GiftsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

@RestController
public class GiftController {

    @Autowired
    GiftService giftService;


    @GetMapping("/product/{number}")
    public void moveKakao(@PathVariable("number") String number, HttpServletResponse response) throws IOException {

        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

    @GetMapping("/gifts")
    public GiftsDTO makeGifts(){
        List<Gift> giftList =giftService.testGetGifts();
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(giftList);
        return giftsDTO;
    }

    @GetMapping("/gifts/search")
    public GiftsDTO searchGifts(@RequestParam(value="search")String search){
        List<Gift> giftList =giftService.searchGifts(search);
        GiftsDTO giftsDTO=new GiftsDTO();
        giftsDTO.setGifts(giftList);
        return giftsDTO;
    }

}
