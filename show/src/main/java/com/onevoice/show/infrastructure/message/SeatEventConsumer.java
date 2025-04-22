package com.onevoice.show.infrastructure.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.application.dto.message.SeatCreateFailMessage;
import com.onevoice.show.application.dto.message.SeatCreateSuccessMessage;
import com.onevoice.show.application.event.GenericKafkaEvent;
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
            GenericKafkaEvent<SeatCreateSuccessMessage> event = objectMapper.readValue(messageJson,
                new TypeReference<GenericKafkaEvent<SeatCreateSuccessMessage>>() {
                }
            );
            SeatCreateSuccessMessage message = event.payload();

            log.info("[seat_create_success] : {}", message);

            sessionService.successToCreateSeat(message.sessionId());

        } catch (Exception e) {
            log.error("Failed to consume seat create success message", e);
        }
    }

    @KafkaListener(topics = "seat_create_fail", groupId = "show")
    public void consumeSeatCreateFail(String messageJson) {
        try {
            GenericKafkaEvent<SeatCreateFailMessage> event = objectMapper.readValue(messageJson,
                new TypeReference<GenericKafkaEvent<SeatCreateFailMessage>>() {
                }
            );
            SeatCreateFailMessage message = event.payload();

            log.info("[seat_create_fail] : {}", message);

            sessionService.failToCreateSeat(message.sessionId());

        } catch (Exception e) {
            log.error("Failed to consume seat create fail message", e);
        }
    }

}
