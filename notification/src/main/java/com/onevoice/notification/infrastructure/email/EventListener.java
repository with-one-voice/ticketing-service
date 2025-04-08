package com.onevoice.notification.infrastructure.email;

import com.onevoice.notification.application.event.EmailSendEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListener implements ApplicationListener<EmailSendEvent> {

    private final JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(EmailSendEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(event.getEmail().title());
        message.setText(getText(event.getEmail().username(), event.getEmail().message()));
        message.setTo(event.getEmail().email());
        javaMailSender.send(message);
    }

    private String getText(String username, String message) {
        return "to: " + username + "," + System.lineSeparator() + System.lineSeparator()
            + message + System.lineSeparator()
            + System.lineSeparator() + System.lineSeparator()
            + "from: " + System.lineSeparator() + "With One Voice";
    }
}
