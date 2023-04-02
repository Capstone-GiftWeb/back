package com.capstone.giftWeb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;
    @NotBlank
    private String token;
    @NotBlank
    private String userId;

    public RefreshToken(String member_id,String token) {
        this.token = token;
        this.userId = member_id;
    }

    public static RefreshToken createToken(String userId, String token){
        return new RefreshToken(userId, token);
    }


    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
