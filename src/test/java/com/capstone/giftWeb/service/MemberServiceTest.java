package com.capstone.giftWeb.service;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberService=new MemberService();
    }

    @Test
    public void 회원가입(){

    }

}
