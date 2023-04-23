package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.config.WebSecurityConfig;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.MemberResponseDto;
import com.capstone.giftWeb.dto.MemberSignUpRequestDto;
import com.capstone.giftWeb.dto.error.CreateError;
import com.capstone.giftWeb.enums.Gender;
import com.capstone.giftWeb.jwt.JwtAccessDeniedHandler;
import com.capstone.giftWeb.jwt.JwtAuthenticationEntryPoint;
import com.capstone.giftWeb.jwt.TokenProvider;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import com.capstone.giftWeb.service.AuthService;
import com.capstone.giftWeb.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
