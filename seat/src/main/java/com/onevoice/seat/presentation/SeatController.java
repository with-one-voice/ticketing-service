package com.onevoice.seat.presentation;


import com.onevoice.common.dto.CommonResponse;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.presentation.dto.request.HoldSeatRequestDto;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import com.onevoice.seat.application.service.SeatService;
import com.onevoice.seat.presentation.dto.request.CreateSeatRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class SeatController {
    private final SeatService seatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CommonResponse<List<SeatCreateResponseDto>>> create(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody CreateSeatRequestDto request) {
        CreateSeatCommand command = new CreateSeatCommand(
                request.sessionId(), request.seatCount(), request.price()
        );
        List<SeatCreateResponseDto> responseList = seatService.createSeat(command);
        return CommonResponse.success(responseList);
    }

    @GetMapping("/{sessionId}/{seatCode}")
    public ResponseEntity<CommonResponse<SeatResponseDto>> getSeat(
            @PathVariable UUID sessionId,
            @PathVariable String seatCode,
            @AuthenticationPrincipal UUID userId
    ) {
        return CommonResponse.success(seatService.getSeat(sessionId, seatCode));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CommonResponse<List<SeatResponseDto>>> getSeatsBySession(
            @PathVariable UUID sessionId,
            @AuthenticationPrincipal UUID userId
    ) {
        return CommonResponse.success(seatService.getSeatBySession(sessionId));
    }

    @PatchMapping("/{sessionId}/hold")
    public ResponseEntity<CommonResponse<HoldSeatResponseDto>> holdSeats(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID sessionId,
            @Valid @RequestBody HoldSeatRequestDto request
    ) {
        HoldSeatCommand command = new HoldSeatCommand(
                sessionId,
                request.seatCodes(),
                userId
        );
        HoldSeatResponseDto result = seatService.holdSeat(command);
        return CommonResponse.success(result);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<CommonResponse<Void>> deleteBySessionId(
            @PathVariable UUID sessionId,
            @AuthenticationPrincipal UUID userId
    ) {
        seatService.deleteSeat(sessionId);
        return CommonResponse.success(null);
    }
}
