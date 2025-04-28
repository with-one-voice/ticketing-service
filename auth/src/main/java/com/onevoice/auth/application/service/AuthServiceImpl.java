package com.onevoice.auth.application.service;

import com.onevoice.auth.application.client.UserClient;
import com.onevoice.auth.application.dto.FindUserQuery;
import com.onevoice.auth.infrastructure.jwt.JwtTokenProvider;
import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import com.onevoice.auth.presentation.dto.response.SignupResponseDto;
import com.onevoice.common.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {

        FindUserQuery query = userClient.signup(requestDto).orElseThrow();

        return SignupResponseDto.of(query);
    }

    @Override
    public String login(LoginRequestDto requestDto) {

        FindUserQuery query = userClient.login(requestDto).orElseThrow();

        return jwtTokenProvider.createAccessToken(query.userId(), UserRole.from(query.role()));
    }
}
