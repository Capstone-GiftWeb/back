package com.capstone.giftWeb.mvc;

import com.capstone.giftWeb.Service.SurveyService;
import com.capstone.giftWeb.controller.MemberController;
import com.capstone.giftWeb.controller.SurveyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileReader;
import java.io.Reader;

import static org.mockito.BDDMockito.*;
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
    public void 설문조사페이지입장() throws Exception {

        ClassPathResource resource = new ClassPathResource("/static/survey.json");
        Reader reader = new FileReader(resource.getFile());
        StringBuffer stringBuffer=new StringBuffer();
        int cur = 0;
        while((cur = reader.read()) != -1){
            stringBuffer.append((char)cur);
        }
        reader.close();
        given(surveyService.parseJson()).willReturn(
                stringBuffer.toString()
        );

        mockMvc.perform(
                get("/survey")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("json"))
                .andDo(print());
    }
}
