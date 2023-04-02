package com.capstone.giftWeb.Service;

import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.domain.RefreshToken;
import com.capstone.giftWeb.dto.MemberLoginRequestDto;
import com.capstone.giftWeb.dto.MemberResponseDto;
import com.capstone.giftWeb.dto.MemberSignUpRequestDto;
import com.capstone.giftWeb.dto.TokenDto;
import com.capstone.giftWeb.dto.error.CreateError;
import com.capstone.giftWeb.jwt.TokenProvider;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String BEARER_TYPE = "bearer";

    public ResponseEntity signup(MemberSignUpRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new CreateError().error("이미 가입되어 있는 유저입니다");
        }

        Member member = requestDto.toMember(passwordEncoder);
        return ResponseEntity.ok(MemberResponseDto.of(memberRepository.save(member)));
    }

    public TokenDto login(MemberLoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }
    //token 앞에 "Bearer-" 제거
    private String resolveToken(String token){
        if(token.startsWith("Bearer"))
            return token.substring(7);
        throw new RuntimeException("not valid refresh token !!");
    }

    public ResponseEntity reIssue(HttpServletRequest request){
        String resolveToken= resolveToken(request.getHeader("Refresh"));
        if (tokenProvider.validateToken(resolveToken)){
            Authentication authentication = tokenProvider.getAuthenticationForReIssue(resolveToken);
            // 디비에 있는게 맞는지 확인
            RefreshToken refreshToken = refreshTokenRepository.findByUserId(authentication.getName()).get();
            if(!resolveToken.equals(refreshToken.getToken())){
                return new CreateError().error("refresh token이 일치하지 않습니다.");
            }

            String newToken = tokenProvider.issueRefreshToken(authentication);  //새로 토큰값을 발급 후 수정
            refreshToken.setToken(newToken);
            refreshTokenRepository.save(refreshToken);

            return ResponseEntity.ok(TokenDto.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(tokenProvider.createAccessToken(authentication))
                    .refreshToken(newToken)
                    .build());
        }
        return new CreateError().error("refresh token이 유효하지 않습니다.");
    }

}