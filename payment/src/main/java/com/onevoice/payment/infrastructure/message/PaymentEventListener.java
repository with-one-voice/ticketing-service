package com.onevoice.payment.infrastructure.message;

import com.onevoice.payment.application.client.TicketClient;
import com.onevoice.payment.application.dto.client.UpdateTicketStatusRequestDto;
import com.onevoice.payment.application.dto.client.UpdateTicketStatusRequestDto.TicketStatus;
import com.onevoice.payment.application.event.PaymentCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j(topic = "PaymentEventListener")
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final TicketClient ticketClient;

    // 데이터 불일치를 방지하기 위해 트랜잭션이 커밋된 이후에만 실행하도록 설정
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventAfterCommit(PaymentCreateEvent event) {
        log.info("PaymentCreateEvent received after commit.");

        // 트랜잭션 커밋 후 실행되는 로직
        ticketClient.updateTicketStatus(
            event.getTicketId(),
            new UpdateTicketStatusRequestDto(TicketStatus.CONFIRM_PAYMENT));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleEventAfterRollback(PaymentCreateEvent event) {
        log.info("PaymentCreateEvent received after rollback.");

        // 트랜잭션 롤백 후 실행되는 로직
        ticketClient.updateTicketStatus(
            event.getTicketId(),
            new UpdateTicketStatusRequestDto(TicketStatus.CANCELLED));
    }
}
