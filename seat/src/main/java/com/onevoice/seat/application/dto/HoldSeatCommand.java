package com.onevoice.seat.application.dto;

import java.util.List;
import java.util.UUID;

public record HoldSeatCommand(
        UUID sessionId,
        List<UUID> seatIds,
        UUID userId
) { }
