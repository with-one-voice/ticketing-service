package com.onevoice.show.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.service.SessionService;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/sessions/{showId}")
    public ResponseEntity<CommonResponse<CreateSessionResponseDto>> create(
        @PathVariable("showId") UUID showId,
        @RequestBody CreateSessionRequestDto requestDto
    ) {

        CreateSessionResponseDto responseDto = sessionService.create(showId, requestDto);
        return CommonResponse.success(responseDto);
    }

    @GetMapping("/sessions")
    public ResponseEntity<CommonResponse<List<SessionResponseDto>>> getAllSessions() {

        List<SessionResponseDto> responseDtoList = sessionService.getAllSessions();
        return CommonResponse.success(responseDtoList);
    }

    @GetMapping("/{showId}/sessions")
    public ResponseEntity<CommonResponse<List<SessionResponseDto>>> getShowSessions(
        @PathVariable("showId") UUID showId
    ) {

        List<SessionResponseDto> responseDtoList = sessionService.getShowSessions(showId);
        return CommonResponse.success(responseDtoList);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<SessionResponseDto>> getOneSession(
        @PathVariable("sessionId") UUID sessionId
    ) {

        SessionResponseDto responseDto = sessionService.getOneSession(sessionId);
        return CommonResponse.success(responseDto);
    }


}
