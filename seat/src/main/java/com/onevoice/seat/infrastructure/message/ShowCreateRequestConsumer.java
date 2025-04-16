package com.onevoice.seat.infrastructure.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.message.SeatCreateRequestMessage;
import com.onevoice.seat.application.event.GenericKafkaEvent;
import com.onevoice.seat.application.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "ShowCreateRequestConsumer")
@Component
@RequiredArgsConstructor
public class ShowCreateRequestConsumer {

    private final ObjectMapper objectMapper;
    private final SeatService seatService;
    private final SeatEventProducer seatEventProducer;

    @KafkaListener(topics = "seat_create_request")
    public void consumeSeatCreateSuccess(String messageJson) {
        try {
            GenericKafkaEvent<SeatCreateRequestMessage> event = objectMapper.readValue(
                messageJson,
                new TypeReference<GenericKafkaEvent<SeatCreateRequestMessage>>() {
                }
            );
            SeatCreateRequestMessage message = event.payload();

            log.info("[seat_create_request] 좌석 생성 요청 이벤트 받음: {}", message);

            // 좌석 생성
            seatService.createSeat(
                new CreateSeatCommand(message.sessionId(), message.seatCount(), message.price()));

//            // 성공 시 -> Show 로 메시지 발행
//            seatEventProducer.sendCreateSuccess(message.sessionId());

        } catch (Exception e) {
            log.error("Failed to consume seat create success message", e);
            // 실패 시 → Show 로 보상 메시지 발행
            try {
                SeatCreateRequestMessage failedMsg = objectMapper.readValue(
                    messageJson, SeatCreateRequestMessage.class
                );
                seatEventProducer.sendCreateFail(failedMsg.sessionId());
            } catch (Exception ex) {
                log.error("seat_create_fail 메시지 전송 실패", ex);
            }
        }
    }
}
