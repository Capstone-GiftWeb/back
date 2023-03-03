package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.auth.AuthInfo;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.error.CreateError;
import com.capstone.giftWeb.dto.LogInCommand;
import com.capstone.giftWeb.dto.SignUpMemberForm;
import com.capstone.giftWeb.enums.Gender;
import com.capstone.giftWeb.exception.IdPasswordNotMatchingException;
import com.capstone.giftWeb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

import static com.capstone.giftWeb.enums.Gender.FEMALE;
import static com.capstone.giftWeb.enums.Gender.MALE;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/new")
    public Object memberSignUp(@Valid SignUpMemberForm memberForm, BindingResult result) {

        if (result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            String errorMessage = allErrors.get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
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

        Member createMember=memberService.createMember(member);
        if (createMember ==null){
            result.rejectValue("email","duplicate","이미 존재하는 이메일입니다.");
            String errorMessage=result.getFieldErrors("email").get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
        }
        return createMember;
    }

    @PostMapping("/login")
    public Object logInMember(@Valid LogInCommand logInCommand, BindingResult result, HttpServletResponse response) throws Exception {

        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            String errorMessage = allErrors.get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
        }
        AuthInfo authInfo;
        try {

            authInfo = memberService.loginAuth(logInCommand);

            Cookie rememberCookie = new Cookie("REMEMBER", logInCommand.getEmail());
            rememberCookie.setPath("/");
            if (logInCommand.isRememberId()) {
                rememberCookie.setMaxAge(60 * 60 * 24 * 7);
            } else {
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);

        } catch (IdPasswordNotMatchingException e) {
            result.rejectValue("password", "notMatch", "아이디와 비밀번호가 맞지않습니다.");
            String errorMessage=result.getFieldErrors("password").get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
        }
        return authInfo;
    }

}
