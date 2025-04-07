package com.onevoice.auth;

import com.onevoice.auth.application.client.UserClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(AuthApplicationTests.MockConfig.class)
@EnableAutoConfiguration(exclude = {org.springframework.cloud.openfeign.FeignAutoConfiguration.class})
class AuthApplicationTests {

	@Autowired
	private UserClient userClient;

	@Test
	void contextLoads() {
		// userClient should be available here
	}

	@TestConfiguration
	static class MockConfig {
		@Bean
		public UserClient userClient() {
			return Mockito.mock(UserClient.class);
		}
	}
}

