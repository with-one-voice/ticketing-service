package com.onevoice.show.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.service.SessionService;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateSessionResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<UpdateSessionResponseDto>> update(
        @PathVariable("sessionId") UUID sessionId,
        @RequestBody UpdateSessionRequestDto requestDto
    ) {

        UpdateSessionResponseDto responseDto = sessionService.update(sessionId, requestDto);
        return CommonResponse.success(responseDto);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<String>> delete(
        @PathVariable("sessionId") UUID sessionId,
        @AuthenticationPrincipal UUID userId
    ) {

        sessionService.delete(sessionId, userId);
        return CommonResponse.success("공연 회차 삭제가 완료되었습니다.");
    }

}
