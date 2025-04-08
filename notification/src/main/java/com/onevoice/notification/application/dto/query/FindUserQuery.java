package com.onevoice.notification.application.dto.query;

import java.util.UUID;

public record FindUserQuery(
    UUID userId,
    String email,
    String password,
    String role
) {

}
