package com.capstone.giftWeb.auth;

import com.capstone.giftWeb.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthInfo {

    private String email;

    private String name;
    private String password;

    private Gender gender;

    private Integer age;



    @Builder
    public AuthInfo(String email,String name, String password,Gender gender,Integer age) {
        this.email = email;
        this.name=name;
        this.password = password;
        this.gender=gender;
        this.age=age;
    }
}
