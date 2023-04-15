package com.capstone.giftWeb.service;

import com.capstone.giftWeb.dto.MailDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void 메일보내기() {
        MailDto mailDto = new MailDto();
        mailDto.setAddress("rmagksfla000@naver.com");
        mailDto.setTitle("Test Email");
        mailDto.setContent("This is a test email.");

        emailService.sendMail(mailDto);
    }
}