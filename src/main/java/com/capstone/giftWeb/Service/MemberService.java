package com.capstone.giftWeb.Service;

import com.capstone.giftWeb.auth.AuthInfo;
import com.capstone.giftWeb.dto.LogInCommand;
import com.capstone.giftWeb.exception.IdPasswordNotMatchingException;
import com.capstone.giftWeb.repository.MemberRepository;
import com.capstone.giftWeb.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Member createMember(Member member) {
        if(!validateDuplicateMember(member)){
            return null;
        }
        memberRepository.save(member);
        return member;
    }

    private boolean validateDuplicateMember(Member member) {
        return memberRepository.findByEmail(member.getEmail())
                .isEmpty();
    }

    public AuthInfo loginAuth(LogInCommand logInCommand) throws Exception {
        Optional<Member> findMember = memberRepository.findByEmail(logInCommand.getEmail());
        Member member;
        if (findMember.isEmpty()) {
            throw new IdPasswordNotMatchingException();
        } else {
            member = findMember.get();
        }
        if (!member.matchPassword(logInCommand.getPassword())) {
            throw new IdPasswordNotMatchingException();
        }
        return AuthInfo.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .gender(member.getGender())
                .age(member.getAge()).build();
    }

}
