package com.onevoice.user.presentation.dto.response;

import com.onevoice.common.security.UserRole;
import com.onevoice.user.domain.User;
import java.util.UUID;

public record GetUserInfoResponseDto(
    UUID userId,
    String email,
    UserRole role
) {
    public static GetUserInfoResponseDto of(User user){
        return new GetUserInfoResponseDto(user.getId(), user.getEmail().getValue(), user.getRole());
    }
}
