package com.onevoice.user.application.dto;

import com.onevoice.user.domain.User;
import java.util.UUID;

public record FindUserQuery(
    UUID userId,
    String email,
    String password,
    String role
) {
    public static FindUserQuery of(User user){
        return new FindUserQuery(user.getId(), user.getEmail().getValue(),
            user.getPassword().getValue(),
            user.getRole().name());
    }
}
