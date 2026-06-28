package com.app.droppr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DropprApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropprApplication.class, args);
	}

}
