package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.RefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void findByToken() {
        // given
        String tokenString="test sample token";
        RefreshToken token = new RefreshToken();
        token.setMemberId(1L);
        token.setToken(tokenString);
        refreshTokenRepository.save(token);

        // when
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByToken(tokenString);

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getToken()).isEqualTo(tokenString);
    }

    @Test
    void testFindByTokenWhenTokenNotFound() {
        // when
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByToken("token123");

        // then
        assertFalse(foundToken.isPresent());
    }
}