package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.service.RecommendService;
import com.capstone.giftWeb.config.SecurityUtil;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.dto.RecommendDto;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController

@RequestMapping("/recommend") // recommend/... 의 주소를 매핑해준다
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping("/")
    public List<Gift> calculate() throws IOException, TasteException, JSONException, SQLException {
        System.out.println("Hello");
        Long userId = SecurityUtil.getCurrentMemberId();

        //this.recommendDto = recommendDto;
        List<Gift> recommendedGiftList = recommendService.recommend(userId, 0L);
        //recommendDto.setRecommendedItemList(recommendedItemList);
        //JSONObject recommendedObj = new JSONObject();
        //recommendedObj.put("recommendedList", recommendedItemList);
        return recommendedGiftList;
        //model.addAttribute("RecommendedList", recommendedItemList);
    }

    @GetMapping("/{categoryNum}")
    public List<Gift> calculate(@PathVariable("categoryNum") String categoryId) throws TasteException, SQLException {
        System.out.println("Hello");
        Long userId = SecurityUtil.getCurrentMemberId();

        //this.recommendDto = recommendDto;
        List<Gift> recommendedGiftList = recommendService.recommend(userId, Long.parseLong(categoryId));
        //recommendDto.setRecommendedItemList(recommendedItemList);
        //JSONObject recommendedObj = new JSONObject();
        //recommendedObj.put("recommendedList", recommendedItemList);
        return recommendedGiftList;
        //model.addAttribute("RecommendedList", recommendedItemList);
    }
}
