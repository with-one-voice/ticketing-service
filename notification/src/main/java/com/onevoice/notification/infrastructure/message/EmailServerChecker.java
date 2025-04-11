package com.onevoice.notification.infrastructure.message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EmailServerChecker")
@Component
public class EmailServerChecker {

    @Value("${spring.mail.host}")
    private String MAIL_HOST;

    @Value("${spring.mail.port}")
    private String MAIL_PORT;

    // 메일 서버 상태 체크
    public boolean isMailServerAvailable() {
        Properties props = new Properties();
        props.put("mail.smtp.host", MAIL_HOST); // 메일 서버 호스트
        props.put("mail.smtp.port", MAIL_PORT); // 메일 서버 포트
        props.put("mail.smtp.connectiontimeout", "3000"); // 타임아웃 설정 (3초)
        props.put("mail.smtp.timeout", "3000"); // 읽기 타임아웃 설정 (3초)

        Session session = Session.getDefaultInstance(props);

        try (Store store = session.getStore("smtp")) {
            store.connect();
            return true;
        } catch (MessagingException e) {
            log.error("Mail server is not available: {}", e.getMessage());
            return false;
        }
    }
}
