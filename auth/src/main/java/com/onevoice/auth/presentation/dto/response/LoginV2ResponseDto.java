package com.onevoice.auth.presentation.dto.response;

import java.util.UUID;

public record LoginV2ResponseDto(
        UUID userId,
        String accessToken
) {
    public static LoginV2ResponseDto of(UUID userId, String accessToken) {
        return new LoginV2ResponseDto(userId, accessToken);
    }
}