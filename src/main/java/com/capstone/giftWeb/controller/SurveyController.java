package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class SurveyController {

    @Autowired
    SurveyService surveyService;

    @GetMapping(value = "/survey")
    public String startSurvey(Model model) throws IOException {
        String json=surveyService.parseJson();
        model.addAttribute("json",json);
        return "survey";
    }
}
