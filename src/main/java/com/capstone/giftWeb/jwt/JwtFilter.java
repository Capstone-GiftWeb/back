package com.capstone.giftWeb.jwt;

import com.capstone.giftWeb.enums.JwtCode;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;



    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String accessToken = resolveToken(request);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals(JwtCode.VALID)) {
            String email= tokenProvider.getEmailFromToken(accessToken);
            Authentication authentication = tokenProvider.createAuthentication(email);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}