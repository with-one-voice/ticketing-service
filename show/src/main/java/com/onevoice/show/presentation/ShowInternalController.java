package com.onevoice.show.presentation;

import com.onevoice.show.application.service.session.SessionService;
import com.onevoice.show.application.service.show.ShowService;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateShowRequestDto;
import com.onevoice.show.presentation.dto.response.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class ShowInternalController {

    private final SessionService sessionService;
    private final ShowService showService;

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

    /**
     * 조회수 상위권 공연 조회
     *
     * @return
     */
    @GetMapping("/top-5")
    public List<ShowResponseDto> getTop5ViewedShows() {

        return showService.getTop5ViewedShows();
    }

    /*
    * 공연 생성
    * */
    @PostMapping("/shows")
    public Optional<CreateShowResponseDto> createShowInternal(@RequestBody CreateShowRequestDto requestDto) {
        return Optional.ofNullable(showService.create(requestDto));
    }

    /*
    * 공연 회차 생성
    * */
    @PostMapping("/sessions/{showId}")
    public Optional<CreateSessionResponseDto> createSessionInternal(
            @PathVariable("showId") UUID showId,
            @RequestBody CreateSessionRequestDto requestDto
    ) {
        return Optional.ofNullable(sessionService.create(showId, requestDto));
    }

    /*
    * 공연 정보 수정
    * */
    @PatchMapping("/{showId}")
    public Optional<UpdateShowResponseDto> updateShowInternal(
            @PathVariable UUID showId,
            @RequestBody UpdateShowRequestDto requestDto
    ) {
        return Optional.ofNullable(showService.update(showId, requestDto));
    }

    /*
    * 공연 회차 정보 수정
    * */
    @PatchMapping("/{sessionId}")
    public Optional<UpdateSessionResponseDto> updateSessionInternal(
            @PathVariable UUID sessionId,
            @RequestBody UpdateSessionRequestDto requestDto
    ) {
        return Optional.ofNullable(sessionService.update(sessionId, requestDto));
    }
}
