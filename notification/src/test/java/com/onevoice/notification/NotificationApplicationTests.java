package com.onevoice.notification;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestMailConfig.class)
class NotificationApplicationTests {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:latest").withExposedPorts(5432);

    private static final GenericContainer<?> redis = new GenericContainer<>(
        "redis:7").withExposedPorts(6379)
        .waitingFor(Wait.forListeningPort());

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

    @Test
    void contextLoads() {

    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
        redis.stop();
    }
}

