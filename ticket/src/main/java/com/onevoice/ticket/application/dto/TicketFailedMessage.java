package com.onevoice.ticket.application.dto;

import java.util.List;
import java.util.UUID;

public record TicketFailedMessage(
    List<UUID> seatIds,
    UUID userId
) {

}
