package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.MemberForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/new")
    public String memberForm() {

        return "member/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(MemberForm memberForm){
        Member member=new Member();
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        member.setPassword(memberForm.getPassword());
        memberService.createMember(member);

        return "redirect:/";
    }

}
