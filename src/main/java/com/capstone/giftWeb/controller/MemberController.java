package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.Service.MemberService;
import com.capstone.giftWeb.domain.Member;
import com.capstone.giftWeb.dto.*;
import com.capstone.giftWeb.dto.error.CreateError;
import com.capstone.giftWeb.enums.Gender;
import com.capstone.giftWeb.exception.IdPasswordNotMatchingException;
import com.capstone.giftWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
@RequestMapping("/member")
@CrossOrigin(originPatterns = "*")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        System.out.println(myInfoBySecurity.getName());
        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }

//    @PostMapping("/name")
//    public ResponseEntity<MemberResponseDto> setMemberName(@RequestBody MemberRequestDto request) {
//        return ResponseEntity.ok(memberService.changeMemberName(request.getEmail(), request.getName()));
//    }

    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberPassword(request.getEmail(),request.getExPassword(), request.getNewPassword()));
    }

}
