package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.auth.AuthInfo;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.LogInCommand;
import com.capstone.giftWeb.dto.SignUpMemberForm;
import com.capstone.giftWeb.exception.IdPasswordNotMatchingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
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
    public String createMember(@Valid SignUpMemberForm memberForm) {
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        member.setPassword(memberForm.getPassword());
        memberService.createMember(member);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String createLogInForm(LogInCommand logInCommand, @CookieValue(value = "REMEMBER",required = false) Cookie rememberCookie, Model model){

        if (rememberCookie!=null){
            logInCommand.setEmail(rememberCookie.getValue());
            logInCommand.setRememberId(true);
            model.addAttribute("logInCommand",logInCommand);
        }

        return "member/logInMemberForm";
    }

    @PostMapping("/login")
    public String logInMember(@Valid LogInCommand logInCommand, Model model,BindingResult bindingResult, HttpSession httpSession, HttpServletResponse response)throws Exception{

        if (bindingResult.hasErrors()){
            return "member/logInMemberForm";
        }

        try {

            AuthInfo authInfo = memberService.loginAuth(logInCommand);
            httpSession.setAttribute("authInfo", authInfo);

            Cookie rememberCookie = new Cookie("REMEMBER", logInCommand.getEmail());
            rememberCookie.setPath("/");
            if(logInCommand.isRememberId()) {
                rememberCookie.setMaxAge(60*60*24*7);
            } else {
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);

        } catch (IdPasswordNotMatchingException e) {
            bindingResult.rejectValue("pw", "notMatch", "아이디와 비밀번호가 맞지않습니다.");
            return "/member/logInMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
