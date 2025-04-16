package com.onevoice.notification.infrastructure.message;

import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.infrastructure.message.handler.NotificationHandler;
import com.onevoice.notification.infrastructure.message.handler.NotificationHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EventListener")
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationHandlerFactory handlerFactory;

    @Async
    @EventListener
    public void handleEvent(NotificationEvent event) {
        NotificationHandler handler = handlerFactory.getHandler(event.notificationType());
        if (handler != null) {
            handler.handle(event);
        } else {
            log.warn("No handler for {}", event.notificationType());
        }
    }
}
