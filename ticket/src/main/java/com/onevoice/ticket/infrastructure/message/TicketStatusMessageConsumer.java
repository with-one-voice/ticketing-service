package com.onevoice.ticket.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.common.enumtype.TicketStatus;
import com.onevoice.ticket.application.dto.TicketMessage;
import com.onevoice.ticket.application.service.TicketService;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketStatusMessageConsumer {

    private final ObjectMapper objectMapper;
    private final TicketService ticketService;

    @KafkaListener(topics = "ticket_status", groupId = "ticket-group")
    public void consume(ConsumerRecord<String, String> record) {
        String value = record.value();
        try {
            TicketMessage message = objectMapper.readValue(value, TicketMessage.class);
            log.info("Received ticket status update: {}", message);

            ticketService.updateTicketStatus(
                message.ticketId(),
                new UpdateTicketStatusRequestDto(TicketStatus.valueOf(message.ticketStatus().name())));

        } catch (IOException e) {
            log.error("Failed to parse ticket status message", e);
        } catch (Exception e) {
            log.error("Failed to update ticket status", e);
        }
    }
}
