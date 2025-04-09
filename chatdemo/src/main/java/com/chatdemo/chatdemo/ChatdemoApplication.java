package com.chatdemo.chatdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
@EnableCaching
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class ChatdemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChatdemoApplication.class, args);
	}
	private final LocalDate date = LocalDate.now();
	@Override
	public void run(String... args) {
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println("---------------CHATDEMO APPLICATION STARTED---------------");
		System.out.println("--------------- "+date+" ---------------");
		System.out.println();
	}

	@Bean
	public RestTemplate restTemplateBean() {
		return new RestTemplate();
	}


}
