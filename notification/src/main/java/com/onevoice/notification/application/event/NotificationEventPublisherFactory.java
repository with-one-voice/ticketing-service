package com.onevoice.notification.application.event;

import com.onevoice.notification.domain.NotificationType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisherFactory {

    private final Map<NotificationType, NotificationEventPublisher> publishers
        = new EnumMap<>(NotificationType.class);

    public NotificationEventPublisherFactory(List<NotificationEventPublisher> publisherList) {
        // 팩토리 생성시 NotificationEventPublisher 를 구현한 모든 클래스를 주입 받아옴.
        // 각 구현체를 타입에 맞게 매핑
        for (NotificationEventPublisher publisher : publisherList) {
            if (publisher instanceof EmailNotificationEventPublisher) {
                publishers.put(NotificationType.EMAIL, publisher);
            }
        }
    }

    // NotificationType 에 맞는 구현체 반환
    public NotificationEventPublisher getPublisher(NotificationType type) {
        return publishers.get(type);
    }
}
