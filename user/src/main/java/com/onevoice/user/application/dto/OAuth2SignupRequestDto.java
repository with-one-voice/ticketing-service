package com.onevoice.user.application.dto;

import com.onevoice.common.security.Provider;

public record OAuth2SignupRequestDto(
    String name,
    String email,
    Provider provider
) {

}
