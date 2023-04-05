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

    @Id
    private String token;
    @NotBlank
    @Indexed
    private String memberEmail;

    public RefreshToken(String token, String memberEmail) {
        this.memberEmail = memberEmail;
        this.token = token;
    }

    public static RefreshToken createToken(String token, String email) {
        return new RefreshToken(token, email);
    }


    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
