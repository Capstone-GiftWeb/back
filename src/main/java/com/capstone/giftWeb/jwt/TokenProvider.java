package com.capstone.giftWeb.jwt;

import com.capstone.giftWeb.service.CustomUserDetailsService;
import com.capstone.giftWeb.dto.TokenDto;
import com.capstone.giftWeb.enums.JwtCode;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60; //1분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; //2주
    private final Key key;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final CustomUserDetailsService customUserDetailsService;


    // 주의점: 여기서 @Value는 `springframework.beans.factory.annotation.Value`소속이다! lombok의 @Value와 착각하지 말것!
    //     * @param secretKey
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository, RefreshTokenRepository refreshTokenRepository1, CustomUserDetailsService userDetailsService) {
        this.customUserDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createAccessToken(String email) {

        Date date = new Date();

        long time = ACCESS_TOKEN_EXPIRE_TIME;
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Date date = new Date();

        long time = REFRESH_TOKEN_EXPIRE_TIME;
        return Jwts.builder()
                .setExpiration(new Date(date.getTime()+time))
                .setIssuedAt(date)
                .signWith(key)
                .compact();
    }

    // 토큰 생성
    public TokenDto createAllToken(String email) {
        return new TokenDto(createAccessToken(email), createRefreshToken());
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        // spring security 내에서 가지고 있는 객체입니다. (UsernamePasswordAuthenticationToken)
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 email 가져오는 기능
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }



    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.VALID;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return JwtCode.MALFORM;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return JwtCode.EXP;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return JwtCode.UNSUPPRT;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return JwtCode.ILLEGAL;
        }
    }

}
