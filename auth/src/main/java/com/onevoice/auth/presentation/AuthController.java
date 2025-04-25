package com.onevoice.auth.presentation;

import com.onevoice.auth.application.service.AuthService;
import com.onevoice.auth.application.service.RedisService;
import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import com.onevoice.auth.presentation.dto.response.LoginResponseDto;
import com.onevoice.auth.presentation.dto.response.SignupResponseDto;
import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisService redisService;

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
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(CommonResponse.createCommonResponse(
                ResponseCode.SUCCESS, new LoginResponseDto("로그인 성공")));
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @RequestHeader("X-Token-Expires") String expires
    ) {
        redisService.blacklistToken(token, Long.parseLong(expires));
        return ResponseEntity.ok()
            .body(CommonResponse.createCommonResponse(
                ResponseCode.SUCCESS, "로그 아웃"
            ));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<CommonResponse<LoginResponseDto>> success(
        @RequestParam("key") String key) {
        String token = redisService.get(key)
            .map(jwt -> {
                // 토큰을 읽어온 후 삭제
                redisService.remove(key);
                return jwt;
            }).orElse(null);
        if (token == null) {
            return ResponseEntity.status(403)
                .body(CommonResponse.createCommonResponse(
                    ResponseCode.FORBIDDEN, new LoginResponseDto("유요하지 않은 OAuth2 세션입니다.")));
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(CommonResponse.createCommonResponse(
                ResponseCode.SUCCESS, new LoginResponseDto("로그인 성공")));
    }

    @GetMapping("/oauth2/failure")
    public ResponseEntity<CommonResponse<String>> failure() {
        log.info("OAuth2 failure");
        return ResponseEntity.status(403)
            .body(CommonResponse.createCommonResponse(
                ResponseCode.FORBIDDEN, "로그인 실패"));
    }
}
