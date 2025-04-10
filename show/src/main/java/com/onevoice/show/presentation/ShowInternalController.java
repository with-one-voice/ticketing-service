package com.onevoice.show.presentation;

import com.onevoice.show.application.service.SessionService;
import com.onevoice.show.presentation.dto.response.SessionDetailResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class ShowInternalController {

    private final SessionService sessionService;

    /**
     * sessionId에 해당하는 공연 회차 상세 정보 조회 (회차ID, 공연ID, 공연명, 회차 날짜, 회차 시작 시간, 회차 종료 시간, 회차 수용 인원, 회차 가격)
     *
     * @param sessionId
     * @return
     */
    @GetMapping("/{sessionId}")
    public Optional<SessionDetailResponseDto> getSessionDetail(
        @PathVariable("sessionId") UUID sessionId
    ) {

        SessionDetailResponseDto responseDto = sessionService.getSessionDetail(sessionId);
        return Optional.ofNullable(responseDto);
    }
}
