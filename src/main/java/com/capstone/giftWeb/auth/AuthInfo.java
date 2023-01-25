package com.capstone.giftWeb.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthInfo {

    private String email;
    private String password;

    public AuthInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
