package com.onevoice.auth;

import com.onevoice.auth.application.client.UserClient;
import com.onevoice.auth.infrastructure.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthApplicationTests.MockConfig.class)
class AuthApplicationTests {

	@Test
	void contextLoads() {

	}

	@TestConfiguration
	static class MockConfig {

		@Bean
		public UserClient userClient() {
			return Mockito.mock(UserClient.class);
		}

		@Bean
		public JwtTokenProvider jwtTokenProvider() {
			return Mockito.mock(JwtTokenProvider.class);
		}
	}
}
