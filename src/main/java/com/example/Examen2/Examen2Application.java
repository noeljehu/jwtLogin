package com.example.Examen2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Examen2Application {

	public static void main(String[] args) {
		SpringApplication.run(Examen2Application.class, args);
	}{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("123");
		System.out.println(encodedPassword);
	}

}
