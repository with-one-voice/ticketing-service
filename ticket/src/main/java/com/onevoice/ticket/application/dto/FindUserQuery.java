package com.onevoice.ticket.application.dto;

import java.util.UUID;

public record FindUserQuery(
        UUID userId,
        String email,
        String role
) {
}
