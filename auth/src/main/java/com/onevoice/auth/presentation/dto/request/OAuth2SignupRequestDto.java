package com.onevoice.auth.presentation.dto.request;

import com.onevoice.common.security.Provider;

public record OAuth2SignupRequestDto(
    String name,
    String email,
    Provider provider
) {

}
