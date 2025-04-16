package com.onevoice.ticket.application.dto;

import com.onevoice.common.enumtype.SeatStatus;
import java.util.List;
import java.util.UUID;

public record UpdateSeatStatusCommand(
    List<UUID> seatIds,
    SeatStatus newStatus
) {
}
