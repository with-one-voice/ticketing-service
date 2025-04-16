package com.onevoice.notification.infrastructure.message.handler;

import com.onevoice.notification.domain.NotificationType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandlerFactory {

    private final Map<NotificationType, NotificationHandler> handlers = new EnumMap<>(
        NotificationType.class);

    // 스프링 부트의 타입기반 주입: 빈으로 등록된 NotificationHandler 를 구현한 모든 클래스 주입
    public NotificationHandlerFactory(List<NotificationHandler> handlerList) {
        for (NotificationHandler handler : handlerList) {
            handlers.put(handler.supports(), handler);
        }
    }

    public NotificationHandler getHandler(NotificationType type) {
        return handlers.get(type);
    }
}
