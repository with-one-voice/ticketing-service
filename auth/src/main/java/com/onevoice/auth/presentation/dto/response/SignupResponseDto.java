package com.onevoice.auth.presentation.dto.response;

import com.onevoice.auth.application.dto.FindUserQuery;
import java.util.UUID;

public record SignupResponseDto(
    UUID userId,
    String email,
    String role
) {

    public static SignupResponseDto of(FindUserQuery userQuery) {
        return new SignupResponseDto(userQuery.userId(), userQuery.email(), userQuery.role());
    }
}
