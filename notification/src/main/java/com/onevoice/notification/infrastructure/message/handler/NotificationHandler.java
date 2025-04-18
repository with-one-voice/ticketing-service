package com.onevoice.notification.infrastructure.message.handler;

import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.domain.NotificationType;

public interface NotificationHandler {

    // 각 구현체가 자신의 구현체를 반환하도록 하여 팩토리에서 타입을 따로 비교하지 않아도 된다.
    NotificationType supports();

    void handle(NotificationEvent event);
}
