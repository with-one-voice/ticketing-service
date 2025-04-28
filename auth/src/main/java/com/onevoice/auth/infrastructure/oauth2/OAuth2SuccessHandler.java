package com.onevoice.auth.infrastructure.oauth2;

import com.onevoice.auth.application.client.UserClient;
import com.onevoice.auth.application.dto.FindUserQuery;
import com.onevoice.auth.application.service.RedisService;
import com.onevoice.auth.infrastructure.jwt.JwtTokenProvider;
import com.onevoice.auth.presentation.dto.request.OAuth2SignupRequestDto;
import com.onevoice.common.security.Provider;
import com.onevoice.common.security.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserClient userClient;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        try {
            // Principal 정보 확인
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            // authentication 객체에서 registrationId 가져오기
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

            // Provider 식별
            Provider provider = Provider.valueOf(registrationId.toUpperCase());

            // 이미 등록된 유저인지 확인
            FindUserQuery query = userClient.findUserByEmail(email)
                .orElseGet(() -> {
                    // 최초 로그인 시 신규 유저 저장 name 을 password 로 사용
                    // TODO: password 방식 변경
                    return userClient.signupByOAuth2(
                            new OAuth2SignupRequestDto(name, email, provider))
                        .orElseThrow(RuntimeException::new);
                });

            // jwt 발급
            String token = jwtTokenProvider.createAccessToken(query.userId(),
                UserRole.from(query.role()));

            // redis 에 저장
            String key = redisService.store(token);
            String redirectUrl = UriComponentsBuilder.fromPath("/api/auth/oauth2/success")
                .queryParam("key", key)
                .build()
                .toUriString();
            log.info("Successfully authenticated user {}", query.userId());
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            log.error("Failed to authenticate user", e);
            response.sendRedirect("/api/auth/oauth2/failure");
        }
    }
}
