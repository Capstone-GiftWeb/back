package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.enums.Authority;
import com.capstone.giftWeb.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberLoginRequestDto {
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;


    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .build();
    }
}