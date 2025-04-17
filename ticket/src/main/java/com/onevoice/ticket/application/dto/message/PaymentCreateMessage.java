package com.onevoice.ticket.application.dto.message;

import java.util.UUID;

public record PaymentCreateMessage(
    UUID ticketId
) {

}
