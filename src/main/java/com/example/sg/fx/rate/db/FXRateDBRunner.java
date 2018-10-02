package com.example.sg.fx.rate.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class FXRateDBRunner {

	public static void main(String[] args) {
		SpringApplication.run(FXRateDBRunner.class, args);
	}

}