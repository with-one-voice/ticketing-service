package com.onevoice.payment.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.common.enumtype.TicketStatus;
import com.onevoice.payment.application.client.TicketClient;
import com.onevoice.payment.application.dto.message.TicketMessage;
import com.onevoice.payment.application.event.PaymentCreateEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j(topic = "PaymentEventListener")
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final TicketClient ticketClient;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // 데이터 불일치를 방지하기 위해 트랜잭션이 커밋된 이후에만 실행하도록 설정
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventAfterCommit(PaymentCreateEvent event) throws JsonProcessingException {
        log.info("PaymentCreateEvent received after commit.");

        // 트랜잭션 커밋 후 실행되는 로직
        // kafka topic 발행으로 변경
//        ticketClient.updateTicketStatus(
//            event.getMessage().ticketId(),
//            new UpdateTicketStatusRequestDto(TicketStatus.CONFIRM_PAYMENT));
        String message = objectMapper.writeValueAsString(event.getMessage());
        String ticketStatus = getTicketStatus(
            event.getMessage().ticketId(),event.getMessage().userId(),
            TicketStatus.CONFIRM_PAYMENT
        );
        // key 는 지정하지 않아도 된다.
        kafkaTemplate.send("notification", message);
        kafkaTemplate.send("ticket_status", ticketStatus);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleEventAfterRollback(PaymentCreateEvent event) throws JsonProcessingException {
        log.info("PaymentCreateEvent received after rollback.");

        // 트랜잭션 롤백 후 실행되는 로직
//        ticketClient.updateTicketStatus(
//            event.getMessage().ticketId(),
//            new UpdateTicketStatusRequestDto(TicketStatus.CANCELLED));
        String ticketStatus = getTicketStatus(
            event.getMessage().ticketId(),event.getMessage().userId(),
            TicketStatus.CANCELLED
        );
        kafkaTemplate.send("ticket_status", ticketStatus);
    }

    private String getTicketStatus(UUID ticketId,UUID userId ,TicketStatus cancelled)
        throws JsonProcessingException {
        return objectMapper.writeValueAsString(
            new TicketMessage(ticketId,userId,
                cancelled)
        );
    }
}
