package com.onevoice.notification.presentation;

import static com.onevoice.notification.fixture.RequestFixture.createFindQuery;
import static com.onevoice.notification.fixture.RequestFixture.invalidRequest;
import static com.onevoice.notification.fixture.RequestFixture.validRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/**
 * NotificationController 테스트 모든 요청 객체는 Fixture 클래스 사용해 생성
 */
@WebMvcTest(NotificationController.class)
class NotificationControllerMvcTest {

    @Autowired
    private MockMvcTester mvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService service;

    @Test
    @DisplayName("알림 생성 요청: 성공 테스트")
    @WithMockUser
    void testCreateSuccess() throws JsonProcessingException {
        // given: 요청 객체 생성은 Fixture 클래스를 사용
        UUID notificationId = UUID.randomUUID();
        var requestBody = objectMapper.writeValueAsString(validRequest());

        given(
            service.create(any(CreateNotificationCommand.class)))
            .willReturn(notificationId);

        // when & then
        assertThat(mvcTester.post().uri("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .with(csrf())
        ).hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .extractingPath("result")
            .isEqualTo("/api/notifications/" + notificationId);
    }

    @Test
    @DisplayName("알림 생성 요청 실패 테스트 - 필수 필드 누락")
    @WithMockUser
    void testCreateFailField() throws JsonProcessingException {
        // given
        UUID notificationId = UUID.randomUUID();
        var createRequest = objectMapper.writeValueAsString(invalidRequest());

        given(
            service.create(any(CreateNotificationCommand.class)))
            .willReturn(notificationId);

        // when & then
        assertThat(mvcTester.post().uri("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequest)
            .with(csrf())
        ).hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .extractingPath("message")
            .isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("알림 목록 조회")
    void testGetList() {
        UUID userId = UUID.randomUUID();

        var notificationList = new ListNotificationQuery(
            List.of(
                createFindQuery(UUID.randomUUID()),
                createFindQuery(UUID.randomUUID())
            )
        );
        given(service.reads(eq(userId), any(Pageable.class)))
            .willReturn(notificationList);

        // when & then
        assertThat(mvcTester.get().uri("/")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            // 요청 userId와 service.reads userId 가 같아야 하므로 @WithMockUser 사용으로는 빈리스트만 받는다
            // authentication 을 임의로 생성해서 요청에 넣어준다.
            .with(authentication(getAuthentication(userId)))
        ).hasStatus(HttpStatus.OK)
            .bodyJson()
            .extractingPath("result.queryList")
            .asArray().hasSize(2);
    }

    @Test
    @DisplayName("알림 상세 조회")
    void testGet() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        var findQuery = createFindQuery(notificationId);

        given(service.read(notificationId, userId))
            .willReturn(findQuery);

        // when & then
        assertThat(mvcTester.get().uri("/" + notificationId)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(authentication(getAuthentication(userId)))
        ).hasStatus(HttpStatus.OK)
            .bodyJson()
            .extractingPath("result")
            .extracting("notificationId",
                "notificationType",
                "notificationStatus",
                "title")
            .containsExactly(notificationId.toString(),
                NotificationType.EMAIL.toString(),
                NotificationStatus.PENDING.toString(),
                "title");
    }

    @Test
    @DisplayName("알림 삭제")
    void testDelete() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();

        assertThat(mvcTester.delete().uri("/" + notificationId)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(authentication(getAuthentication(userId)))
        ).hasStatus(HttpStatus.NO_CONTENT);
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(UUID userId) {
        return new UsernamePasswordAuthenticationToken(
            userId,  // UUID 직접 주입
            "password",
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}