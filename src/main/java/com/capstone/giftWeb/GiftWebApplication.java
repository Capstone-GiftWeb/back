package com.capstone.giftWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GiftWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftWebApplication.class, args);
	}

}
