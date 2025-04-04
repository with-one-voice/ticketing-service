package com.onevoice.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:application-test.yml"})
class AuthApplicationTests {

	@Autowired
	Environment env;

	@PostConstruct
	void init() {
		System.out.println(">>> Active profile: " + Arrays.toString(env.getActiveProfiles()));
		System.out.println(">>> Property: config.enabled = " + env.getProperty("spring.cloud.config.enabled"));
		System.out.println(">>> Property: eureka.client.enabled = " + env.getProperty("eureka.client.enabled"));
	}

	@Test
	void contextLoads() {
	}
}
