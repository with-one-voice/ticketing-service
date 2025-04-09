package com.onevoice.user.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.application.service.UserService;
import com.onevoice.user.presentation.dto.request.DeleteUserRequestDto;
import com.onevoice.user.presentation.dto.response.DeleteUserResponseDto;
import com.onevoice.user.presentation.dto.response.GetUserInfoResponseDto;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Optional<FindUserQuery> signup(@RequestBody SignupRequestDto command) {
        return userService.signup(command);
    }

    @PostMapping("/login")
    public Optional<FindUserQuery> login(@RequestBody LoginRequestDto command) {
        return userService.login(command);
    }

    @GetMapping("/{userId}")
    public Optional<FindUserQuery> findUserById(@PathVariable UUID userId) {
        return userService.findUserById(userId);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<GetUserInfoResponseDto>> getMyInfo(
        @AuthenticationPrincipal UUID userId) {

        GetUserInfoResponseDto responseDto = userService.getMyInfo(userId);
        return CommonResponse.success(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<DeleteUserResponseDto>> deleteUser(
        @AuthenticationPrincipal UUID userId, @RequestBody DeleteUserRequestDto requestDto) {

        DeleteUserResponseDto responseDto = userService.deleteUser(userId, requestDto);
        return CommonResponse.success(responseDto);
    }
}
