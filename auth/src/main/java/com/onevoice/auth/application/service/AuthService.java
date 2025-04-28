package com.onevoice.auth.application.service;

import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import com.onevoice.auth.presentation.dto.response.LoginResponseDto;
import com.onevoice.auth.presentation.dto.response.LoginV2ResponseDto;
import com.onevoice.auth.presentation.dto.response.SignupResponseDto;

public interface AuthService {

    SignupResponseDto signup(SignupRequestDto requestDto);

    String login(LoginRequestDto requestDto);
    LoginV2ResponseDto loginWithUserId(LoginRequestDto requestDto);
}
