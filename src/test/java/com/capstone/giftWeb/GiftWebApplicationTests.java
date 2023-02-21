package com.capstone.giftWeb;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.enums.Gender;
import com.capstone.giftWeb.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@Transactional
class GiftWebApplicationTests {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;


	@Test
	public void 회원가입(){
		String email="123j1oo1ijo213";
		Member member = new Member();
		member.setEmail(email);
		member.setPassword("1234");
		member.setName("금한림");
		member.setGender(Gender.MALE);
		member.setAge(25);
		memberService.createMember(member);

		Member findMember=memberRepository.findByEmail(email).get();

		assertThat(member).isEqualTo(findMember);
	}

	@Test
	public void 회원중복가입(){
		String email="123j1oo1ijo213";
		Member member = new Member();
		member.setEmail(email);
		member.setPassword("1234");
		member.setName("금한림");
		member.setGender(Gender.MALE);
		member.setAge(25);
		memberService.createMember(member);

		Member member1 = new Member();
		member.setEmail(email);
		member.setPassword("1234");
		member.setName("금한림");
		member.setGender(Gender.MALE);
		member.setAge(25);
		try{
			memberService.createMember(member1);
		} catch (IllegalStateException e){
			assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
		}
	}

}
