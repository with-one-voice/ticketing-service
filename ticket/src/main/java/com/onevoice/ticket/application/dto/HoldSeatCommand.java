package com.onevoice.ticket.application.dto;

import java.util.List;
import java.util.UUID;

public record HoldSeatCommand(
    List<UUID> seatIds
) {

}
