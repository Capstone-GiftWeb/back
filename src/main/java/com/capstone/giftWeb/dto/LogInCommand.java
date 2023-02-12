package com.capstone.giftWeb.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LogInCommand {

    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    private boolean rememberId;
}
