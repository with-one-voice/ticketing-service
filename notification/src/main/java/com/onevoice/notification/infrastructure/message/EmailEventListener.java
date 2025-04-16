package com.onevoice.notification.infrastructure.message;

import com.onevoice.notification.application.event.EmailSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EventListener")
@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailSender emailSender;

    @Async
    @EventListener
    public void handleEvent(EmailSendEvent event) {
        emailSender.send(event.getContext());
    }
}
