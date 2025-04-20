package com.onevoice.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.payment.fixture.AuthHeaderFixture;
import com.onevoice.payment.fixture.RequestFixture;
import java.net.URI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentApplicationTests {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:latest").withExposedPorts(5432);

    private static final GenericContainer<?> redis = new GenericContainer<>(
        "redis:7").withExposedPorts(6379)
        .waitingFor(Wait.forListeningPort());

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void startContainers() {
        postgres.start();
        redis.start();
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @BeforeAll
    static void setupLoggingBridge() {
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
    }


    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("결제 요청")
    void paymentSuccess() {
        // given
        var request = RequestFixture.validRequest();
        var headers = AuthHeaderFixture.user();

        // when
        ResponseEntity<CommonResponse<URI>> response = restTemplate
            .exchange(
                "/",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<CommonResponse<URI>>() {
                }
            );

        // then: assertEquals(expected, actual)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
        redis.stop();
    }
}
