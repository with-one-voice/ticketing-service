package com.onevoice.seat.presentation;


import com.onevoice.common.dto.CommonResponse;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import com.onevoice.seat.application.service.SeatService;
import com.onevoice.seat.presentation.dto.request.CreateSeatRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class SeatController {
    private final SeatService seatService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse<List<SeatResponseDto>>> create(@Valid @RequestBody CreateSeatRequestDto request) {
        CreateSeatCommand command = new CreateSeatCommand(
                request.sessionId(), request.seatCount(), request.price()
        );
        List<SeatResponseDto> responseList = seatService.createSeats(command);
        return CommonResponse.success(responseList);
    }
    @GetMapping("/{sessionId}/{seatCode}")
    public ResponseEntity<CommonResponse<SeatResponseDto>> getSeat(
            @PathVariable UUID sessionId,
            @PathVariable String seatCode
    ) {
        return CommonResponse.success(seatService.getSeat(sessionId, seatCode));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CommonResponse<List<SeatResponseDto>>> getSeatsBySession(
            @PathVariable UUID sessionId
    ) {
        return CommonResponse.success(seatService.getSeatsBySession(sessionId));
    }


}
