package com.onevoice.auth;

import com.onevoice.auth.application.client.UserClient;
import com.onevoice.auth.infrastructure.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(AuthApplicationTests.MockConfig.class)
@EnableFeignClients(clients = {})
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
