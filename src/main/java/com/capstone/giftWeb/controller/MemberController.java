package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.service.MemberService;
import com.capstone.giftWeb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
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
