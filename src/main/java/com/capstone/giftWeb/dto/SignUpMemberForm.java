package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.enums.Gender;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class SignUpMemberForm {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotEmpty(message="성별을 입력해주세요.")
    private String gender;
    private Integer age;
}
