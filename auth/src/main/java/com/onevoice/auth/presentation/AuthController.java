package com.onevoice.auth.presentation;

import com.onevoice.auth.application.service.AuthService;
import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import com.onevoice.auth.presentation.dto.response.LoginResponseDto;
import com.onevoice.auth.presentation.dto.response.SignupResponseDto;
import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponseDto>> signup(
        @RequestBody SignupRequestDto requestDto) {

        SignupResponseDto responseDto = authService.signup(requestDto);
        return CommonResponse.success(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(
        @RequestBody LoginRequestDto requestDto) {

        String token = authService.login(requestDto);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(CommonResponse.createCommonResponse(
                ResponseCode.SUCCESS,new LoginResponseDto("로그인 성공")));
    }

}
