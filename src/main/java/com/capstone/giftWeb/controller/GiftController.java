package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.config.SecurityUtil;
import com.capstone.giftWeb.service.GiftService;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.dto.GiftsDto;
import com.capstone.giftWeb.dto.WordsDto;
import com.capstone.giftWeb.enums.Field;
import com.capstone.giftWeb.enums.GiftOrderType;
import com.capstone.giftWeb.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RestController
public class GiftController {

    @Autowired
    GiftService giftService;

    @Autowired
    RecommendService recommendService;


    @GetMapping("/product/{number}/liked")
    public void likesGift(@PathVariable("number") String number, HttpServletResponse response) throws SQLException, IOException {
        Long userId = SecurityUtil.getCurrentMemberId();
        recommendService.giftLiked(userId, Long.parseLong(number));
        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

    @GetMapping("/product/{number}")
    public void moveKakao(@PathVariable("number") String number, HttpServletResponse response) throws IOException, SQLException {
        Long userId = SecurityUtil.getCurrentMemberId();
        recommendService.giftClicked(userId, Long.parseLong(number));

        response.sendRedirect("https://gift.kakao.com/product/"+number);
    }

    @GetMapping("/gifts")
    public GiftsDto makeGifts(){
        List<Gift> giftList =giftService.testGetGifts();
        GiftsDto giftsDTO=new GiftsDto();
        giftsDTO.setGifts(giftList);
        return giftsDTO;
    }

    @GetMapping("/gifts/wordSearchShow.action")
    public WordsDto wordSearchShow(@RequestParam("searchWord") String searchWord){
        List<String> wordList;
        wordList=giftService.wordSearchShow(searchWord);
        WordsDto wordsDto=new WordsDto();
        if(wordList!=null){
            wordsDto.setWords(wordList);
        }
        return wordsDto;
    }

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
