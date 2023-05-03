package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByEmail() {
        // given
        String email = "test@example.com";
        Member member = Member.builder()
                .email(email)
                .password("test1234")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // then
        assertTrue(foundMember.isPresent());
        assertEquals(email, foundMember.get().getEmail());
    }

    @Test
    void existsByEmail() {
        // Given
        Member member = new Member();
        member.setEmail("test@example.com");
        memberRepository.save(member);

        // When
        boolean exists = memberRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }
}