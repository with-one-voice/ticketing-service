package com.onevoice.seat.infrastructure.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.seat.application.dto.message.TicketConfirmFailedMessage;
import com.onevoice.seat.application.dto.message.TicketConfirmedMessage;
import com.onevoice.seat.application.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketMessageConsumer {
    private final ObjectMapper objectMapper;
    private final SeatService seatService;


    @KafkaListener(topics = "ticket_confirm", groupId = "seat-group")
    public void consumeTicketConfirm(String messageJson){
        try {
            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode payload = root.get("payload");

            TicketConfirmedMessage message = objectMapper.treeToValue(payload, TicketConfirmedMessage.class);

            log.info("ticket_confirm] 수신: {}", message);
            seatService.confirmSeats(message.seatIds(), message.userId());

        } catch (Exception e) {
            log.error("ticket_confirm 파싱 실패", e);
        }
    }

    @KafkaListener(topics = "ticket_cancel", groupId = "seat-group")
    public void consumeTicketConfirmFail(String messageJson) {
        try {
            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode payload = root.get("payload");

            TicketConfirmFailedMessage message = objectMapper.treeToValue(payload, TicketConfirmFailedMessage.class);

            log.info("[ticket_cancel] 수신: {}", message);
            seatService.revertSeats(message.seatIds(), message.userId());

        } catch (Exception e) {
            log.error("ticket_cancel 파싱 실패", e);
        }
    }
}
