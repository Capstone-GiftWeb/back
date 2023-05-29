package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.enums.Authority;
import com.capstone.giftWeb.enums.Gender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberDto {

    private String name;
    private String email;
    private Gender gender;
    private Authority authority;

    public MemberDto(Member writer){
        this.name= writer.getName();
        this.email= writer.getEmail();
        this.gender = writer.getGender();
        this.authority=writer.getAuthority();
    }

}
