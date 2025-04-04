package com.onevoice.auth;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTests {

	@Autowired
	Environment env;

	@PostConstruct
	void logEnv() {
		System.out.println("🔎 active profiles: " + Arrays.toString(env.getActiveProfiles()));
		System.out.println("🔎 spring.config.import: " + env.getProperty("spring.config.import"));
		System.out.println("🔎 spring.cloud.config.enabled: " + env.getProperty("spring.cloud.config.enabled"));
	}

	@Test
	void contextLoads() {
	}
}
