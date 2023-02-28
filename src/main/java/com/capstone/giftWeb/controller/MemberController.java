package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.auth.AuthInfo;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.LogInCommand;
import com.capstone.giftWeb.dto.SignUpMemberForm;
import com.capstone.giftWeb.enums.Gender;
import com.capstone.giftWeb.exception.IdPasswordNotMatchingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.capstone.giftWeb.enums.Gender.FEMALE;
import static com.capstone.giftWeb.enums.Gender.MALE;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/new")
    public String memberForm(Model model) {

        model.addAttribute("signUpMemberForm",new SignUpMemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@Valid @ModelAttribute("signUpMemberForm") SignUpMemberForm memberForm, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()){
            return "member/createMemberForm";
        }

        Gender gender = null;
        if (memberForm.getGender().equals("남자")){
            gender= MALE;
        }else if(memberForm.getGender().equals("여자")){
            gender=FEMALE;
        }
        Member member = Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .password(memberForm.getPassword())
                .gender(gender)
                .age(memberForm.getAge()).build();
        memberService.createMember(member);


        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String createLogInForm(LogInCommand logInCommand, @CookieValue(value = "REMEMBER", required = false) Cookie rememberCookie, Model model) {

        if (rememberCookie != null) {
            logInCommand.setEmail(rememberCookie.getValue());
            logInCommand.setRememberId(true);
        }
        model.addAttribute("logInCommand", logInCommand);

        return "member/logInMemberForm";
    }

    @PostMapping("/login")
    public String logInMember(@Valid @ModelAttribute("logInCommand") LogInCommand logInCommand, BindingResult bindingResult, HttpSession httpSession, HttpServletResponse response) throws Exception {

        if (bindingResult.hasErrors()) {
            return "member/logInMemberForm";
        }

        try {

            AuthInfo authInfo = memberService.loginAuth(logInCommand);
            httpSession.setAttribute("authInfo", authInfo);

            Cookie rememberCookie = new Cookie("REMEMBER", logInCommand.getEmail());
            rememberCookie.setPath("/");
            if (logInCommand.isRememberId()) {
                rememberCookie.setMaxAge(60 * 60 * 24 * 7);
            } else {
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);

        } catch (IdPasswordNotMatchingException e) {
            bindingResult.rejectValue("password", "notMatch", "아이디와 비밀번호가 맞지않습니다.");
            return "/member/logInMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
