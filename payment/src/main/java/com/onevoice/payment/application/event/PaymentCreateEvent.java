package com.onevoice.payment.application.event;

import com.onevoice.payment.application.dto.message.PaymentMessage;
import java.io.Serial;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentCreateEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final PaymentMessage message;

    public PaymentCreateEvent(Object source, PaymentMessage message) {
        super(source);
        this.message = message;
    }
}
