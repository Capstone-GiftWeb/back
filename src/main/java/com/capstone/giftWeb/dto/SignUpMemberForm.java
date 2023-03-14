package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.enums.Gender;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
public class SignUpMemberForm {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotEmpty(message="성별을 입력해주세요.")
    private String gender;
    @NotNull(message="나이를 입력해주세요.")
    @Positive(message = "0이나 음수는 입력할 수 없습니다.")
    private Integer age;
}
