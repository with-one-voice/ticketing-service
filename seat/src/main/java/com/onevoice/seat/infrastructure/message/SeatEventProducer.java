package com.onevoice.seat.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.common.enumtype.KafkaTopicType;
import com.onevoice.seat.application.dto.message.SeatCreateFailMessage;
import com.onevoice.seat.application.dto.message.SeatCreateSuccessMessage;
import com.onevoice.seat.application.dto.message.SeatExpiredMessage;
import com.onevoice.seat.application.event.GenericKafkaEvent;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SeatEventProducer")
@Component
@RequiredArgsConstructor
public class SeatEventProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTransactionalEventPublisher kafkaTransactionalEventPublisher;

    public void sendCreateSuccess(UUID sessionId) {
        try {
            SeatCreateSuccessMessage payload = new SeatCreateSuccessMessage(sessionId);
            GenericKafkaEvent<SeatCreateSuccessMessage> event = new GenericKafkaEvent<>(
                KafkaTopicType.SEAT_CREATE_SUCCESS.getTopic(), payload);
            kafkaTransactionalEventPublisher.handleKafkaMessage(event);
            log.info("seat_create_success 전송 완료: {}", payload);
        } catch (Exception e) {
            log.error("seat_create_success 전송 실패", e);
        }
    }

    public void sendCreateFail(UUID sessionId) {
        try {
            SeatCreateFailMessage payload = new SeatCreateFailMessage(sessionId);
            GenericKafkaEvent<SeatCreateFailMessage> event = new GenericKafkaEvent<>(
                KafkaTopicType.SEAT_CREATE_FAIL.getTopic(), payload);
            kafkaTransactionalEventPublisher.handleKafkaMessage(event);
            log.info("seat_create_fail 전송 완료: {}", payload);
        } catch (Exception e) {
            log.error("seat_create_fail 전송 실패", e);
        }
    }

    /*
     * 좌석 만료
     * */
    public void sendSeatExpired(List<UUID> seatIds, UUID userId) {

        try {
            SeatExpiredMessage message = new SeatExpiredMessage(seatIds, userId);
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(KafkaTopicType.SEAT_EXPIRED.getTopic(), userId.toString(),
                jsonMessage);
            log.info("seat_expired 전송 완료: {}", jsonMessage);
        } catch (Exception e) {
            log.error("seat_expired 전송 실패", e);
        }

    }

}
