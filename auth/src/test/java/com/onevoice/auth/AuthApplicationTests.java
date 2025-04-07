package com.onevoice.auth;

import com.onevoice.auth.application.client.UserClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(AuthApplicationTests.MockConfig.class)
@EnableAutoConfiguration(exclude = {org.springframework.cloud.openfeign.FeignAutoConfiguration.class})
class AuthApplicationTests {

	private final UserClient userClient;

	AuthApplicationTests(UserClient userClient) {
		this.userClient = userClient;
	}


	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class MockConfig {
		@Bean
		public UserClient userClient() {
			return Mockito.mock(UserClient.class);
		}
	}
}
