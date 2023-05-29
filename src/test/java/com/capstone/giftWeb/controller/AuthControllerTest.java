package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.response.MemberResponseDto;
import com.capstone.giftWeb.dto.request.MemberSignUpRequestDto;
import com.capstone.giftWeb.enums.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void 회원가입성공() throws Exception {
        // Given
        MemberSignUpRequestDto requestDto = new MemberSignUpRequestDto();
        String password = "test1234";
        requestDto.setName("test");
        requestDto.setPassword(password);
        requestDto.setEmail("test@example.com");
        requestDto.setGender("남자");
        requestDto.setAge(23);

        Member testMember = Member.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .gender(Gender.MALE)
                .age(requestDto.getAge())
                .build();
        // When
        ResultActions resultActions = mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
        );
        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(MemberResponseDto.of(testMember))))
                .andDo(print());
    }

    @Test
    public void 회원가입실패() throws Exception {
        // Given
        MemberSignUpRequestDto requestDto = new MemberSignUpRequestDto();
        requestDto.setName("");
        requestDto.setPassword("test1234");
        requestDto.setEmail("test@example.com");
        requestDto.setGender("남자");
        requestDto.setAge(23);

        String expectedErrorMessage = "이름을 입력해주세요.";

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(expectedErrorMessage))
                .andDo(print());
    }
}
