package com.onevoice.seat.application.dto;


import java.util.UUID;

public record CreateSeatCommand(UUID sessionId, int seatCount, int price) {

}


