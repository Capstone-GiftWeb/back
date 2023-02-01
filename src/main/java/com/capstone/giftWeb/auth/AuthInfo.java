package com.capstone.giftWeb.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthInfo {

    private String email;

    private String name;
    private String password;



    @Builder
    public AuthInfo(String email,String name, String password) {
        this.email = email;
        this.name=name;
        this.password = password;
    }
}
