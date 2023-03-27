package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.Service.SurveyService;
import com.capstone.giftWeb.controller.MemberController;
import com.capstone.giftWeb.controller.SurveyController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
@AutoConfigureWebMvc
public class SurveyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SurveyService surveyService;

    @Test
    public void 설문제이슨테스트() throws Exception {
        ClassPathResource resource = new ClassPathResource("/static/survey.json");
        Reader reader = new FileReader(resource.getFile());
        StringBuffer stringBuffer = new StringBuffer();
        int cur = 0;
        while ((cur = reader.read()) != -1) {
            stringBuffer.append((char) cur);
        }
        reader.close();
        String returnString = stringBuffer.toString();

        MvcResult result = mockMvc.perform(
                        get("/survey/data")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(returnString)
                ).andExpect(status().isOk()).andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andDo(print()).andReturn();

    }
}
