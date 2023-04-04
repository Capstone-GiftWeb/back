package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.RecommendService;
import com.capstone.giftWeb.dto.RecommendDto;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recommend") // recommend/... 의 주소를 매핑해준다
public class RecommendController {
    private RecommendService recommendService;
    private RecommendDto recommendDto;

    @GetMapping("/")
    public String recommend() {
        return "/result";
    }

    @GetMapping("/result")
    public void calculate(Model model, RecommendDto recommendDto, @RequestParam Long userId) throws IOException, TasteException {
        List<RecommendedItem> recommendedItemList = recommendDto.getRecommendedItemList();
        //model.addAttribute("RecommendedList", recommendedItemList);
    }
}
