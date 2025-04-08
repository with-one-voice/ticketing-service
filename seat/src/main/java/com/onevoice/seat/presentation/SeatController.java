package com.onevoice.seat.presentation;


import com.onevoice.common.dto.CommonResponse;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import com.onevoice.seat.application.service.SeatService;
import com.onevoice.seat.presentation.dto.request.CreateSeatRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
