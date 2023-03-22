package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.GiftService;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.dto.GiftsDto;
import com.capstone.giftWeb.dto.WordsDto;
import com.capstone.giftWeb.enums.Field;
import com.capstone.giftWeb.enums.GiftOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GiftController {

    @Autowired
    GiftService giftService;


    @GetMapping("/product/{number}")
    public void moveKakao(@PathVariable("number") String number, HttpServletResponse response) throws IOException {

        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

    @GetMapping("/gifts")
    public GiftsDto makeGifts(){
        List<Gift> giftList =giftService.testGetGifts();
        GiftsDto giftsDTO=new GiftsDto();
        giftsDTO.setGifts(giftList);
        return giftsDTO;
    }

//    @GetMapping("/gifts/wordSearchShow.action")
//    public WordsDto wordSearchShow(HttpServletRequest request){
//        String searchType = request.getParameter("searchType");
//        String searchWord = request.getParameter("searchWord");
//
//        Map<String, String> paraMap = new HashMap<>();
//        paraMap.put("searchType", searchType);
//        paraMap.put("searchWord", searchWord);
//
//        List<String> wordList;
//        wordList=giftService.wordSearchShow(paraMap);
//        WordsDto wordsDto=new WordsDto();
//        if(wordList!=null){
//            wordsDto.setWords(wordList);
//        }
//        return wordsDto;
//    }

    @GetMapping("/gifts/search")
    public GiftsDto searchGifts(@RequestParam(value="search")String search, @RequestParam(value="field",required = false)Field field,@RequestParam(value="order",required = false) GiftOrderType order){
        List<Gift> giftList =giftService.searchGifts(search);
        if (field!=null) {
            if (field.equals(Field.PRICE)) {
                if(order==null||order.equals(GiftOrderType.ASC)) {
                    giftList.sort((o1, o2) -> o1.getPrice() - o2.getPrice());
                } else if (order.equals(GiftOrderType.DESC)) {
                    giftList.sort((o1, o2) -> o2.getPrice() - o1.getPrice());
                }
            } else if (field.equals(Field.TITLE)) {
                if (order==null||order.equals(GiftOrderType.ASC)) {
                    Collections.sort(giftList, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
                } else if (order.equals(GiftOrderType.DESC)) {
                    Collections.sort(giftList, (o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
                }
            }
        }
        GiftsDto giftsDTO=new GiftsDto();
        giftsDTO.setGifts(giftList);
        return giftsDTO;
    }

}
