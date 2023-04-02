package com.capstone.giftWeb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
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

    public RefreshToken(String userId,String token) {
        this.token = token;
        this.userId = userId;
    }

    public static RefreshToken createToken(String userId, String token){
        return new RefreshToken(userId, token);
    }


    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
