package com.onevoice.auth.presentation;

import com.onevoice.auth.application.service.AuthService;
import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import com.onevoice.auth.presentation.dto.response.LoginResponseDto;
import com.onevoice.auth.presentation.dto.response.SignupResponseDto;
import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisTemplate<String, Object> redisTemplate;

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

    @GetMapping("/oauth2/success")
    public ResponseEntity<CommonResponse<LoginResponseDto>> success(
        @RequestParam("key") String key) {
        String token = Optional.ofNullable(redisTemplate.opsForValue().get(key))
            .map(jwt -> {
                redisTemplate.delete(key);
                return jwt.toString();
            }).orElse(null);
        log.info("Successfully oauth2 login");
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
