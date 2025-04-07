package com.onevoice.auth.presentation.dto.request;

public record LoginRequestDto(
    String email,
    String password
) {

}
