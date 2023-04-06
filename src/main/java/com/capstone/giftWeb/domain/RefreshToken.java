package com.capstone.giftWeb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 14)
public class RefreshToken {

    @NotBlank
    @Id
    private String memberEmail;

    @Indexed
    private String token;
    public RefreshToken(String memberEmail,String token) {
        this.memberEmail = memberEmail;
        this.token = token;
    }

    public static RefreshToken createToken(String memberEmail,String token) {
        return new RefreshToken(memberEmail,token);
    }


    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
