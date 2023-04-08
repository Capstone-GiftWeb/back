package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.RefreshToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void save() {
    }

    @Test
    void findById() {
        String refreshToken="refresh!!";
    }

    @Test
    void findByMemberEmail() {
        String email="rmagksfla@naver.com";
        RefreshToken refreshToken=new RefreshToken("123123",email);
        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> findRefreshToken= refreshTokenRepository.findById(email);

        refreshTokenRepository.delete(refreshToken);

        assertEquals(refreshToken.getMemberEmail(),findRefreshToken.get().getMemberEmail());
        assertEquals(refreshToken.getToken(),findRefreshToken.get().getToken());
    }
}