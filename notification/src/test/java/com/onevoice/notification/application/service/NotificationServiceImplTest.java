package com.onevoice.notification.application.service;

import static com.onevoice.notification.fixture.RequestFixture.validRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.notification.domain.repository.NotificationRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

class NotificationServiceImplTest {

    private final NotificationServiceImpl service;

    public NotificationServiceImplTest() {
        NotificationRepository repository = new StubNotificationRepository();

        // event 는 테스트 시 사용하지 않으므로 임의의 객체 생성
        ApplicationEventPublisher publisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
            }
        };
        this.service = new NotificationServiceImpl(repository, publisher);
    }

    @Test
    @DisplayName("create")
    void testCreate() {
        // given
        var command = validRequest().to(UUID.randomUUID());

        // when
        UUID notificationId = service.create(command);

        // then
        assertThat(notificationId).isNotNull();
    }

    // TODO: 나머지 메서드 테스트
}