package com.capstone.giftWeb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpMemberForm {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
