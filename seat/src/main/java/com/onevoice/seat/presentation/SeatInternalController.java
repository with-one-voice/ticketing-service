package com.onevoice.seat.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.application.service.SeatService;
import com.onevoice.seat.presentation.dto.request.CreateSeatRequestDto;
import com.onevoice.seat.presentation.dto.request.HoldSeatRequestDto;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/seat")
public class SeatInternalController {
    private final SeatService seatService;


    /*
     * 회차별 좌석 조회
     * */
    @GetMapping("/{sessionId}")
    public List<SeatResponseDto> getSeatsBySession(
            @PathVariable("sessionId") UUID sessionId
    ) {
        return seatService.getSeatBySession(sessionId);
    }

    /*
     * 좌석 생성
     * */
    @PostMapping("/create")
    public Optional<List<SeatCreateResponseDto>> createInternal(
            @RequestBody @Valid CreateSeatRequestDto request
    ) {
        CreateSeatCommand command = new CreateSeatCommand(
                request.sessionId(), request.seatCount(), request.price()
        );
        List<SeatCreateResponseDto> responseList = seatService.createSeat(command);
        return Optional.ofNullable(responseList);
    }

    /*
     * 좌석 선점
     * */
    @PatchMapping("/{sessionId}/hold")
    public Optional<HoldSeatResponseDto> holdSeatsInternal(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID sessionId,
            @RequestBody HoldSeatRequestDto request
    ) {
        HoldSeatCommand command = new HoldSeatCommand(
                sessionId,
                request.seatCodes(),
                userId
        );
        HoldSeatResponseDto result = seatService.holdSeat(command);
        return Optional.ofNullable(result);
    }
}
