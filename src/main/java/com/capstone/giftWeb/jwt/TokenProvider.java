package com.capstone.giftWeb.jwt;

import com.capstone.giftWeb.Service.CustomUserDetailsService;
import com.capstone.giftWeb.domain.RefreshToken;
import com.capstone.giftWeb.dto.TokenDto;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60*60*24 * 14; //2주
    private final Key key;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomUserDetailsService customUserDetailsService;



    // 주의점: 여기서 @Value는 `springframework.beans.factory.annotation.Value`소속이다! lombok의 @Value와 착각하지 말것!
    //     * @param secretKey
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService userDetailsService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.customUserDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }


    // 토큰 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        String accessToken=createAccessToken(authentication);
        String refreshToken=createRefreshToken(authentication);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date tokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key)
                .compact();
    }


    public String createRefreshToken(Authentication authentication){
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .signWith(key)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Transactional
    public String issueRefreshToken(Authentication authentication){
        String newRefreshToken = createRefreshToken(authentication);

        // 기존것이 있다면 바꿔주고, 없다면 만들어줌
        refreshTokenRepository.findByUserId(authentication.getName())
                .ifPresentOrElse(
                        r-> {r.updateToken(newRefreshToken);
                            log.info("issueRefreshToken method | change token ");
                        },
                        () -> {
                            RefreshToken token = RefreshToken.createToken(authentication.getName(), newRefreshToken);
                            log.info(" issueRefreshToken method | save tokenID : {}, token : {}", token.getUserId(), token.getToken());
                            refreshTokenRepository.save(token);
                        });

        return newRefreshToken;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
