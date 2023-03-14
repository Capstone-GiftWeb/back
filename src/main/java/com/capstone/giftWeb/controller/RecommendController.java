package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.RecommendService;
import com.capstone.giftWeb.dto.RecommendDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
public class RecommendController {
    @GetMapping("/recommend")
    public String recommend() {
        return "recommend/start";
    }

    @PostMapping("/recommend/result")
    public String getUserId(Model model, @RequestParam("userId") String userId) {
        model.addAttribute("userId", userId);
        return "recommend/result";
    }


}
