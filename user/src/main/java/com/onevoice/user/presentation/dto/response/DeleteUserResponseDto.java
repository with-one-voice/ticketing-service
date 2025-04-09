package com.onevoice.user.presentation.dto.response;

import com.onevoice.common.security.UserRole;
import com.onevoice.user.domain.User;
import java.util.UUID;

public record DeleteUserResponseDto(
    UUID userId,
    String email,
    UserRole role
) {
    public static DeleteUserResponseDto of(User user){
        return new DeleteUserResponseDto(user.getId(), user.getEmail().getValue(), user.getRole());
    }
}
