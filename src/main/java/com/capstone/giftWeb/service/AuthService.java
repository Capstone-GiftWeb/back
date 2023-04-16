package com.capstone.giftWeb.service;

import com.capstone.giftWeb.config.SecurityUtil;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.domain.RefreshToken;
import com.capstone.giftWeb.dto.*;
import com.capstone.giftWeb.dto.error.CreateError;
import com.capstone.giftWeb.enums.JwtCode;
import com.capstone.giftWeb.jwt.TokenProvider;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private static final String BEARER_TYPE = "bearer";

    public ResponseEntity signup(HttpServletRequest request,MemberSignUpRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new CreateError().error("이미 가입되어 있는 유저입니다");
        }

        Member member = requestDto.toMember(passwordEncoder);
        member.setIp(SecurityUtil.getClientIp(request));
        memberRepository.save(member);
        return ResponseEntity.ok(MemberResponseDto.of(member));
    }

    public void logout(){
        Long userId= SecurityUtil.getCurrentMemberId();
        log.info(userId+" logout.");
        Optional<RefreshToken> refreshToken= refreshTokenRepository.findById(userId);
        if (refreshToken.isPresent()){
            refreshTokenRepository.delete(refreshToken.get());
        }
    }

    public ResponseEntity login(HttpServletRequest request, MemberLoginRequestDto requestDto) {
        // 아이디 검사
        Optional<Member> member = memberRepository.findByEmail(requestDto.getEmail());
        if (member.isEmpty()) {
            return new CreateError().error("이메일이 맞지 않습니다.");
        }
        String exIp=member.get().getIp();
        String newIp=SecurityUtil.getClientIp(request);
        if (!exIp.equals(newIp)){ //최근에 로그인했던 ip와 현재 로그인한 ip가 다를 경우
            sendMail(member,exIp,newIp);
        }


        // 비밀번호 검사
        if (!passwordEncoder.matches(requestDto.getPassword(), member.get().getPassword())) {
            return new CreateError().error("비밀번호가 맞지 않습니다.");
        }

        // 아이디 정보로 Token생성
        TokenDto tokenDto = tokenProvider.createAllToken(requestDto.getEmail());

        // Refresh토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(member.get().getId());

        // 있다면 새토큰 발급후 업데이트
        // 없다면 새로 만들고 디비 저장
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken.get());
        } else {
            RefreshToken newToken = RefreshToken.builder()
                    .memberId(member.get().getId())
                    .token(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(newToken);
        }
        return ResponseEntity.ok(tokenDto);
    }

    private void sendMail(Optional<Member> member,String exIp,String newIp){
        log.info("exClientIp : "+exIp + " newClientIp : "+newIp);
        MailDto mailDto = new MailDto();
        mailDto.setAddress(member.get().getEmail());
        mailDto.setTitle("기존 ip와 다른 ip에서 접속한 것이 발견되었습니다.");
        mailDto.setContent("새로운 ip : "+newIp);

        emailService.sendMail(mailDto);
    }

    //token 앞에 "Bearer-" 제거
    private String resolveToken(String token) {
        if (token.startsWith("Bearer"))
            return token.substring(7);
        throw new RuntimeException("유효하지 않은 토큰입니다.");
    }

    public ResponseEntity reIssue(HttpServletRequest request) {
        String resolveToken = resolveToken(request.getHeader("refresh"));
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByToken(resolveToken);
        JwtCode code = tokenProvider.validateToken(resolveToken);

        if (findRefreshToken.isPresent() && code.equals(JwtCode.VALID)) {

            // 리프레시 토큰으로 아이디 정보 가져오기
            Long memberId = findRefreshToken.get().getMemberId();
            Optional<Member> member = memberRepository.findById(memberId);
            String email = member.get().getEmail();
            // 새로운 어세스 토큰 발급
            String newAccessToken = tokenProvider.createAccessToken(email);

            TokenDto tokenDto = TokenDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(resolveToken)
                    .build();
            return ResponseEntity.ok(tokenDto);
        } else if (code.equals(JwtCode.ILLEGAL)) {
            return new CreateError().error("JWT 토큰이 잘못되었습니다.");
        } else if (code.equals(JwtCode.MALFORM)) {
            return new CreateError().error("잘못된 JWT 서명입니다.");
        } else if (code.equals(JwtCode.UNSUPPRT)) {
            return new CreateError().error("지원되지 않는 JWT 토큰입니다.");
        } else if (code.equals(JwtCode.EXP)) {
            return new CreateError().error("만료된 JWT 토큰입니다.");
        }
        return new CreateError().error("JWT 토큰이 잘못되었습니다.");
    }

}