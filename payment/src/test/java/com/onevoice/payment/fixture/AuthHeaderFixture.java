package com.onevoice.payment.fixture;

import java.util.UUID;
import org.springframework.http.HttpHeaders;

public class AuthHeaderFixture {

    public static HttpHeaders user() {
        return create(UUID.randomUUID(), "USER");
    }

    public static HttpHeaders admin() {
        return create(UUID.randomUUID(), "ADMIN");
    }

    public static HttpHeaders custom(UUID userId, String role) {
        return create(userId, role);
    }

    private static HttpHeaders create(UUID userId, String role) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        headers.set("X-User-Role", role);
        return headers;
    }
}
