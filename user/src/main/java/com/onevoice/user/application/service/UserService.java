package com.onevoice.user.application.service;

import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.presentation.dto.request.DeleteUserRequestDto;
import com.onevoice.user.presentation.dto.response.DeleteUserResponseDto;
import com.onevoice.user.presentation.dto.response.GetUserInfoResponseDto;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<FindUserQuery> signup(SignupRequestDto command);

    Optional<FindUserQuery> login(LoginRequestDto command);

    Optional<FindUserQuery> findUserById(UUID userId);

    GetUserInfoResponseDto getMyInfo(UUID userId);

    DeleteUserResponseDto deleteUser(UUID userId, DeleteUserRequestDto requestDto);
}
