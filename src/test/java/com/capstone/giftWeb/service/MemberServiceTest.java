package com.capstone.giftWeb.service;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void 회원가입(){
        Member member = new Member();
        member.setEmail("rmagksfla@naver.com");
        member.setPassword("1234");
        member.setName("금한림");
        memberService.createMember(member);

        Member findMember=memberRepository.findByEmail("rmagksfla@naver.com").get();

        assertThat(member).isEqualTo(findMember);
    }

}
