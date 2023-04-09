package com.capstone.giftWeb.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 14) // 2주
public class RefreshToken {

    @NotBlank
    @Id
    private Long memberId;

    private String ip;

    @Indexed
    private String token;
    public RefreshToken(Long memberId,String token) {
        this.memberId=memberId;
        this.token = token;
    }

    public static RefreshToken createToken(Long memberId,String token) {
        return new RefreshToken(memberId,token);
    }


    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
