package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.RecommendService;
import com.capstone.giftWeb.dto.RecommendDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;
    private RecommendDto recommendDto;

    @GetMapping("/")
    public String recommend() {
        return "recommend/start";
    }

    @PostMapping("/calculate")
    public void calculate(Model model, RecommendDto recommendDto, @RequestParam Long userId) throws IOException, TasteException {
        List<RecommendedItem> recommendedItemList = recommendDto.getRecommendedItemList();
        //model.addAttribute("RecommendedList", recommendedItemList);
    }
}
