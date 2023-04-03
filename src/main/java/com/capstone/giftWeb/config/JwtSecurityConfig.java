package com.capstone.giftWeb.config;

import com.capstone.giftWeb.jwt.JwtFilter;
import com.capstone.giftWeb.jwt.TokenProvider;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider,refreshTokenRepository);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
