package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;

import java.util.List;


public interface SeatService {
    List<SeatResponseDto> createSeats(CreateSeatCommand command);
}
