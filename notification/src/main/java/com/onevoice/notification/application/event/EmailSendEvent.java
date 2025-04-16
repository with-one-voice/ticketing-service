package com.onevoice.notification.application.event;

import com.onevoice.notification.application.dto.message.EmailContext;
import java.io.Serial;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailSendEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = -2685172945219633123L;

    private final EmailContext context;

    public EmailSendEvent(Object source, EmailContext context) {
        super(source);
        this.context = context;
    }
}
