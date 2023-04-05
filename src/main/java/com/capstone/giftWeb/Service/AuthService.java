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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
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

    public ResponseEntity login(MemberLoginRequestDto requestDto) {
        // 아이디 검사
        Optional<Member> member = memberRepository.findByEmail(requestDto.getEmail());
        if (member.isEmpty()) {
            return new CreateError().error("아이디가 맞지 않습니다.");
        }

        // 비밀번호 검사
        if (!passwordEncoder.matches(requestDto.getPassword(), member.get().getPassword())) {
            return new CreateError().error("비밀번호가 맞지 않습니다.");
        }

        // 아이디 정보로 Token생성
        TokenDto tokenDto = tokenProvider.createAllToken(requestDto.getEmail());

        // Refresh토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(requestDto.getEmail());

        // 있다면 새토큰 발급후 업데이트
        // 없다면 새로 만들고 디비 저장
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(tokenDto.getRefreshToken());
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(),requestDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        return ResponseEntity.ok(tokenDto);
    }

    //token 앞에 "Bearer-" 제거
    private String resolveToken(String token) {
        if (token.startsWith("Bearer"))
            return token.substring(7);
        throw new RuntimeException("not valid refresh token !!");
    }

    public ResponseEntity reIssue(HttpServletRequest request) {
        String resolveToken = resolveToken(request.getHeader("refresh"));
        Optional<RefreshToken> findRefreshToken=refreshTokenRepository.findById(resolveToken);
        if (findRefreshToken.isPresent()&&tokenProvider.validateToken(resolveToken)&& tokenProvider.getStringFromRefreshToken(resolveToken).startsWith("refresh.")) {

            // 리프레시 토큰으로 아이디 정보 가져오기
            String email = tokenProvider.getEmailFromRefreshToken(resolveToken);
            // 새로운 어세스 토큰 발급
            String newAccessToken = tokenProvider.createToken(email, "access");

            TokenDto tokenDto = TokenDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(resolveToken)
                    .build();
            return ResponseEntity.ok(tokenDto);
        }
        return new CreateError().error("refresh token이 유효하지 않습니다.");
    }

}