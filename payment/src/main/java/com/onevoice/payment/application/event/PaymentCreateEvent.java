package com.onevoice.payment.application.event;

import java.io.Serial;
import java.util.UUID;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentCreateEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID ticketId;

    public PaymentCreateEvent(Object source, UUID ticketId) {
        super(source);
        this.ticketId = ticketId;
    }
}
