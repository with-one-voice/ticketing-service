package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;

import java.util.List;
import java.util.UUID;


public interface SeatService {
    List<SeatResponseDto> createSeats(CreateSeatCommand command);
    SeatResponseDto getSeat(UUID sessionId, String seatCode);

    List<SeatResponseDto> getSeatsBySession(UUID sessionId);
}
