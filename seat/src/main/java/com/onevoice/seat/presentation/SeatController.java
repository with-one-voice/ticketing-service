package com.onevoice.seat.presentation;


import com.onevoice.common.dto.CommonResponse;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.presentation.dto.request.HoldSeatRequestDto;
import com.onevoice.seat.presentation.dto.request.SeatStatusChangeRequestDto;
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

    /*
    * 좌석 생성
    * */
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

    /*
    * 좌석 단건 조회
    * */
    @GetMapping("/{sessionId}/{seatCode}")
    public ResponseEntity<CommonResponse<SeatResponseDto>> getSeat(
            @PathVariable UUID sessionId,
            @PathVariable String seatCode,
            @AuthenticationPrincipal UUID userId
    ) {
        return CommonResponse.success(seatService.getSeat(sessionId, seatCode));
    }

    /*
    * 회차별 좌석 목록 조회
    * */
    @GetMapping("/{sessionId}")
    public ResponseEntity<CommonResponse<List<SeatResponseDto>>> getSeatsBySession(
            @PathVariable UUID sessionId,
            @AuthenticationPrincipal UUID userId
    ) {
        return CommonResponse.success(seatService.getSeatBySession(sessionId));
    }

    /*
    * 좌석 선점 요청
    * */
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

    /*
    * 좌석 상태 변경
    * */
    @PatchMapping("/status")
    public ResponseEntity<CommonResponse<List<SeatResponseDto>>> updateStatusExternal(
            @RequestBody SeatStatusChangeRequestDto request
    ) {
        List<SeatResponseDto> updatedSeats = seatService.updateSeatStatuses(
                request.seatIds(), request.newStatus()
        );
        return CommonResponse.success(updatedSeats);
    }

    /*
    * 좌석 삭제
    * */
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
