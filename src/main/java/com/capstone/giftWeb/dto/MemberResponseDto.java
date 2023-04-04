package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private String email;
    private String name;
    private Gender gender;
    private int age;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .build();
    }
}