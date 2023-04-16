package com.capstone.giftWeb.service;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.MemberResponseDto;
import com.capstone.giftWeb.dto.MemberSignUpRequestDto;
import com.capstone.giftWeb.jwt.TokenProvider;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;


    @BeforeEach
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 회원가입성공() {
        // given
        String email = "test@test.com";
        String password = "test1234";
        String gender = "남자";
        String name = "테스트유저";
        int age= 25;
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email(email)
                .password(password)
                .gender(gender)
                .age(age)
                .name(name)
                .build();

        Member member= requestDto.toMember(passwordEncoder);

        when(memberRepository.existsByEmail(email)).thenReturn(false);
        doReturn(member).when(memberRepository).save(member);

        // when
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("HTTP_CLIENT_IP","127.0.0.1");
        ResponseEntity response = authService.signup(request,requestDto);

        // then
        Member saveMember=memberRepository.save(member);
        assertThat(saveMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(saveMember.getAge()).isEqualTo(member.getAge());
        assertThat(saveMember.getName()).isEqualTo(member.getName());
        assertThat(saveMember.getGender()).isEqualTo(member.getGender());
        verify(memberRepository).existsByEmail(email);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(MemberResponseDto.class);
        MemberResponseDto responseBody = (MemberResponseDto) response.getBody();
        assertThat(responseBody.getEmail()).isEqualTo(email);
    }

}