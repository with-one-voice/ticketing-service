package com.onevoice.ticket.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FindHoldSeatQuery (
    String message,
    LocalDateTime expireAt,
    List<UUID>seatIds
){

}
