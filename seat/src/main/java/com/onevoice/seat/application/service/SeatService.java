package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.domain.SeatStatus;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;

import java.util.List;
import java.util.UUID;


public interface SeatService {
    List<SeatCreateResponseDto> createSeat(CreateSeatCommand command);
    SeatResponseDto getSeat(UUID sessionId, String seatCode);

    List<SeatResponseDto> getSeatBySession(UUID sessionId);
    HoldSeatResponseDto holdSeat(HoldSeatCommand command);

    void deleteSeat(UUID sessionId);
    List<SeatResponseDto> updateSeatStatuses(List<UUID> seatIds, SeatStatus newStatus);


}
