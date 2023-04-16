package com.capstone.giftWeb.service;

import com.capstone.giftWeb.dto.MailDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void 메일보내기() throws ExecutionException, InterruptedException {
        MailDto mailDto = new MailDto();
        mailDto.setAddress("dlwjddus8958@naver.com");
        mailDto.setTitle("안녕");
        mailDto.setContent("나는 한림이야. 테스트 메일을 보내볼게. 얍!");

        CompletableFuture<String> future=emailService.sendMail(mailDto);

        String result = future.get();
        assertThat(result).isEqualTo("send succeed");
    }
}