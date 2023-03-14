package com.capstone.giftWeb.mvc;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.controller.MemberController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureWebMvc
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Test
    public void 로그인페이지입장() throws Exception {

        mockMvc.perform(
                get("/members/login")
        ).andExpect(status().isOk());
    }

    @Test
    public void 회원가입페이지입장() throws Exception{

        mockMvc.perform(
                get("/members/new")
        ).andExpect(status().isOk());
    }

}
