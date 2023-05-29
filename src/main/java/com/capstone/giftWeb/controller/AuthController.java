package com.capstone.giftWeb.controller;

import com.capstone.giftWeb.service.AuthService;
import com.capstone.giftWeb.dto.request.MemberLoginRequestDto;
import com.capstone.giftWeb.dto.request.MemberSignUpRequestDto;
import com.capstone.giftWeb.dto.error.CreateError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(HttpServletRequest request,@RequestBody @Valid MemberSignUpRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String errorMessage = allErrors.get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
        }


        return authService.signup(request,requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(
            HttpServletRequest request,
            @RequestBody @Valid MemberLoginRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String errorMessage = allErrors.get(0).getDefaultMessage();
            return new CreateError().error(errorMessage);
        }

        return authService.login(request,requestDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(){
        authService.logout();


        return ResponseEntity.ok("logout succeed");
    }

    @PostMapping("/reissue")
    public ResponseEntity reIssue(HttpServletRequest request){
        return authService.reIssue(request);
    }
}