package com.onevoice.auth.application.dto;

import java.util.UUID;

public record FindUserQuery (
    UUID userId,
    String email,
    String password,
    String role
){

}
