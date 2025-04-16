package com.onevoice.show.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.application.dto.message.SeatCreateFailMessage;
import com.onevoice.show.application.dto.message.SeatCreateSuccessMessage;
import com.onevoice.show.application.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SeatEventConsumer")
@Component
@RequiredArgsConstructor
public class SeatEventConsumer {

    private final ObjectMapper objectMapper;
    private final SessionService sessionService;

    @KafkaListener(topics = "seat_create_success", groupId = "show")
    public void consumeSeatCreateSuccess(String messageJson) {
        try {
            SeatCreateSuccessMessage message = objectMapper.readValue(messageJson,
                SeatCreateSuccessMessage.class);

            log.info("[seat_create_success] : {}", message);

            sessionService.successToCreateSeat(message.sessionId());

        } catch (Exception e) {
            log.error("Failed to consume seat create success message", e);
        }
    }

    @KafkaListener(topics = "seat_create_fail", groupId = "show")
    public void consumeSeatCreateFail(String messageJson) {
        try {
            SeatCreateFailMessage message = objectMapper.readValue(messageJson,
                SeatCreateFailMessage.class);

            log.info("[seat_create_fail] : {}", message);

            sessionService.failToCreateSeat(message.sessionId());

        } catch (Exception e) {
            log.error("Failed to consume seat create fail message", e);
        }
    }

}
