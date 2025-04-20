package com.onevoice.notification.infrastructure;

import static com.onevoice.notification.fixture.NotificationFixture.createNotification;
import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.notification.domain.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(NotificationRepositoryImpl.class)
@DataJpaTest
class NotificationRepositoryImplTest {

    @Autowired
    NotificationRepositoryImpl repository;

    @Test
    void test() {
        // 설정이 불러와 지는지 확인
    }

    @Test
    @DisplayName("저장")
    void testSave() {
        // given
        Notification notification = createNotification();

        // when
        Notification saved = repository.save(notification);

        // then
        assertThat(saved.getUserId()).isEqualTo(notification.getUserId());
    }

    // TODO: 나머지 메서드 테스트
}