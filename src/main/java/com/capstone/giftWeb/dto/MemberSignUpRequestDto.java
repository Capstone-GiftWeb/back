package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.enums.Authority;
import com.capstone.giftWeb.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignUpRequestDto {
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message="성별을 입력해주세요.")
    @Pattern(regexp = "^(남자|여자)$", message = "성별은 '남자' 또는 '여자'만 입력 가능합니다.")
    private String gender;
    @NotNull(message="나이를 입력해주세요.")
    @Positive(message = "0이나 음수는 입력할 수 없습니다.")
    private int age;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;


    public Member toMember(PasswordEncoder passwordEncoder) {
        Gender convert_gender;
        if (gender.equals("남자")){
            convert_gender=Gender.MALE;
        }else{
            convert_gender=Gender.FEMALE;
        }
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .name(name)
                .age(age)
                .gender(convert_gender)
                .build();
    }

}