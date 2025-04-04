package com.onevoice.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTests {

	@Autowired
	Environment env;

	@PostConstruct
	void init() {
		System.out.println(">>> Active profile: " + Arrays.toString(env.getActiveProfiles()));
	}

	@Test
	void contextLoads() {
	}
}
