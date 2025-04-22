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
    @PutMapping("/shows/{showId}")
    public Optional<UpdateShowResponseDto> updateShowInternal(
            @PathVariable UUID showId,
            @RequestBody UpdateShowRequestDto requestDto
    ) {
        return Optional.ofNullable(showService.update(showId, requestDto));
    }

    /*
    * 공연 회차 정보 수정
    * */
    @PutMapping("/sessions/{sessionId}")
    public Optional<UpdateSessionResponseDto> updateSessionInternal(
            @PathVariable UUID sessionId,
            @RequestBody UpdateSessionRequestDto requestDto
    ) {
        return Optional.ofNullable(sessionService.update(sessionId, requestDto));
    }


    /*
    * 공연 전체 조회
    * */
    @GetMapping("/shows")
    public List<ShowResponseDto> getAllShowsInternal() {
        return showService.getAll();
    }

    /*
    * 공연 단건 조회
    * */
    @GetMapping("/shows/{showId}")
    public ShowResponseDto getShowDetailInternal(@PathVariable UUID showId) {
        return showService.getOne(showId);
    }
    /*
    * 회차 전체 조회
    */
    @GetMapping("/sessions")
    public List<SessionResponseDto> getAllSessionsInternal() {
        return sessionService.getAllSessions();
    }

    /*
     * 회차 단건 조회
     */
    @GetMapping("/sessions/{sessionId}")
    public SessionResponseDto getSessionDetailInternal(@PathVariable UUID sessionId) {
        return sessionService.getOneSession(sessionId);
    }
    /*
     * 공연 삭제
     */
    @DeleteMapping("/shows/{showId}")
    public void deleteShowInternal(
            @PathVariable UUID showId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        showService.delete(showId, userId);
    }

    /*
     * 공연 회차 삭제
     */
    @DeleteMapping("/sessions/{sessionId}")
    public void deleteSessionInternal(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        sessionService.delete(sessionId, userId);
    }

}
