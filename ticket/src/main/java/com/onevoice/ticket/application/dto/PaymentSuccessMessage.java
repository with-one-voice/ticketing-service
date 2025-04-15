package com.onevoice.ticket.application.dto;

import java.util.UUID;

public record PaymentSuccessMessage(
    UUID ticketId
) {

}
