package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping(value = "/data" ,produces= "application/json; charset=UTF-8")
    public ResponseEntity parseJson() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        return new ResponseEntity(surveyService.parseJson(),headers,HttpStatus.OK);
    }

}
