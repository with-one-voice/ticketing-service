package com.onevoice.ticket;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class TicketApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(TicketApplicationTests.class);

	@Autowired
	Environment env;

	@BeforeEach
	void logEnv() {
		log.info("🔎 active profiles: {}", Arrays.toString(env.getActiveProfiles()));
		log.info("🔎 spring.config.import: {}", env.getProperty("spring.config.import"));
		log.info("🔎 spring.cloud.config.enabled: {}", env.getProperty("spring.cloud.config.enabled"));
	}

	@Test
	void contextLoads() {
		// 빈 테스트
		log.info("🔎 active profiles: {}", Arrays.toString(env.getActiveProfiles()));
		log.info("🔎 spring.config.import: {}", env.getProperty("spring.config.import"));
		log.info("🔎 spring.cloud.config.enabled: {}", env.getProperty("spring.cloud.config.enabled"));
	}
}
