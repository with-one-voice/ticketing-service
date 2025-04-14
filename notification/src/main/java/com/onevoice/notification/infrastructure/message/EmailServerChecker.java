package com.onevoice.notification.infrastructure.message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EmailServerChecker")
@Component
public class EmailServerChecker {

    @Value("${spring.mail.host}")
    private String MAIL_HOST;

    @Value("${spring.mail.port}")
    private String MAIL_PORT;

    @Value("${spring.mail.username}")
    private String MAIL_USERNAME;

    @Value("${spring.mail.password}")
    private String MAIL_PASSWORD;

    // 메일 서버 상태 체크(Redis Cache TTL: 5 minute)
    @Cacheable(value = "mailServerAvailability")
    public boolean isMailServerAvailable() {
        Properties props = new Properties();
        props.put("mail.smtp.host", MAIL_HOST); // 메일 서버 호스트
        props.put("mail.smtp.port", MAIL_PORT); // 메일 서버 포트
        props.put("mail.smtp.auth", "true"); // SMTP 인증 사용
        props.put("mail.smtp.connectiontimeout", "3000"); // 접속 타임아웃 설정 (3초)
        props.put("mail.smtp.timeout", "3000"); // 읽기 타임아웃 설정 (3초)
        props.put("mail.smtp.starttls.enable", "true"); // TLS 사용

        Session session = Session.getDefaultInstance(props);

        try (Transport transport = session.getTransport("smtp")) {
            transport.connect(
                MAIL_HOST,
                Integer.parseInt(MAIL_PORT),
                MAIL_USERNAME,
                MAIL_PASSWORD);
            log.info("Mail server is available");
            return true;
        } catch (MessagingException e) {
            log.error("Mail server is not available: {}", e.getMessage());
            return false;
        }
    }
}
