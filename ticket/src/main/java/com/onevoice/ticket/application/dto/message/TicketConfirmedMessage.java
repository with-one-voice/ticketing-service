package com.onevoice.ticket.application.dto.message;

import java.util.List;
import java.util.UUID;

public record TicketConfirmedMessage(
    List<UUID> seatIds,
    UUID userId
) {

}
