package com.onevoice.show.application.service;

import com.onevoice.show.application.client.SeatClient;
import com.onevoice.show.application.client.VenueClient;
import com.onevoice.show.application.dto.CreateSeatRequestDto;
import com.onevoice.show.application.dto.FindSessionQuery;
import com.onevoice.show.application.dto.VenueResponseDto;
import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.Status;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.CancelledShowException;
import com.onevoice.show.exception.DuplicateSessionException;
import com.onevoice.show.exception.InvalidSeatCountException;
import com.onevoice.show.exception.InvalidSessionDateException;
import com.onevoice.show.exception.InvalidVenueIdException;
import com.onevoice.show.exception.NotFoundSessionException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.exception.SeatCreateApiFailException;
import com.onevoice.show.exception.TicketingAlreadyStartedException;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionDetailResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateSessionResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final ShowRepository showRepository;
    private final SessionRepository sessionRepository;
    private final VenueClient venueClient;
    private final SeatClient seatClient;

    @Override
    @Transactional
    public CreateSessionResponseDto create(UUID showId, CreateSessionRequestDto requestDto) {

        Show show = showRepository.findById(showId).orElseThrow(NotFoundShowException::new);

        // 공연 상태 확인 (취소 상태인 공연의 경우 회차 등록 불가능)
        if (show.getStatus() != null) {
            if (show.getStatus().equals(Status.CANCELLED)) {
                throw new CancelledShowException();
            }
        }

        // 공연 예매 일자와 공연 회차 일자 비교
        if (!show.getTicketingEndTime().toLocalDate().isBefore(requestDto.sessionDate())) {
            throw new InvalidSessionDateException();
        }

        // 해당 공연의 동일 날짜에 해당하는 회차 존재 여부 확인
        if (sessionRepository.find(showId, requestDto.sessionDate()).isPresent()) {
            throw new DuplicateSessionException();
        }

        // 공연장 총 수용 인원과 회차 별 수용 인원 비교
        VenueResponseDto venue = venueClient.getVenueOne(show.getVenueId()).orElseThrow(
            InvalidVenueIdException::new);
        if (venue.totalSeatCount() < requestDto.seatCount()) {
            throw new InvalidSeatCountException();
        }

        Session session = Session.builder()
            .show(show)
            .sessionDate(requestDto.sessionDate())
            .startTime(requestDto.startTime())
            .endTime(requestDto.endTime())
            .seatCount(requestDto.seatCount())
            .seatPrice(requestDto.seatPrice())
            .build();

        FindSessionQuery query = FindSessionQuery.of(sessionRepository.save(session));

        //TODO: 좌석 생성 FeignClient 호출 -> seat와 api 맞추면 해결 될 듯 ??
        try {
            seatClient.create(new CreateSeatRequestDto(session.getId(), session.getSeatCount(),
                session.getSeatPrice().intValue()));
        } catch (Exception e) {
            throw new SeatCreateApiFailException();
        }

        return CreateSessionResponseDto.of(query);
    }

    @Override
    public List<SessionResponseDto> getAllSessions() {

        List<FindSessionQuery> queries = sessionRepository.findAll().stream()
            .map(FindSessionQuery::of).toList();

        return queries.stream().map(SessionResponseDto::of).toList();
    }

    @Override
    public List<SessionResponseDto> getShowSessions(UUID showId) {

        if (showRepository.findById(showId).isEmpty()) {
            throw new NotFoundShowException();
        }

        List<FindSessionQuery> queries = sessionRepository.findByShowId(showId).stream()
            .map(FindSessionQuery::of).toList();

        return queries.stream().map(SessionResponseDto::of).toList();
    }

    @Override
    public SessionResponseDto getOneSession(UUID sessionId) {

        FindSessionQuery query = FindSessionQuery.of(
            sessionRepository.findById(sessionId).orElseThrow(NotFoundSessionException::new));

        return SessionResponseDto.of(query);
    }

    @Override
    @Transactional
    public UpdateSessionResponseDto update(UUID sessionId, UpdateSessionRequestDto requestDto) {

        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(NotFoundSessionException::new);

        // 이미 예매가 진행된 경우 -> 공연 회차 정보 수정 불가
        if (!session.getShow().getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        // (같은 공연 내) 수정한 회차 날짜에 이미 다른 회차 정보가 존재하는 경우 -> 회차 정보 수정 불가
        List<Session> sessions = sessionRepository.findByShowId(session.getShow().getId());
        sessions.forEach(s -> {
            if (!s.getId().equals(sessionId)) {
                if (s.getSessionDate().equals(requestDto.sessionDate())) {
                    throw new DuplicateSessionException();
                }
            }
        });

        session.update(requestDto);

        return UpdateSessionResponseDto.of(FindSessionQuery.of(session));
    }

    @Override
    @Transactional
    public void delete(UUID sessionId, UUID userId) {

        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(NotFoundSessionException::new);

        // 이미 예매가 진행된 경우 -> 공연 회차 삭제 불가
        if (!session.getShow().getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        session.delete(userId);
    }

    @Override
    @Transactional
    public void updateStatus(UUID sessionId) {

        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(NotFoundSessionException::new);

        // 이미 예매가 진행된 경우 -> 공연 회차 상태 변경 불가
        if (!session.getShow().getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        session.updateStatus();
    }

    @Override
    public SessionDetailResponseDto getSessionDetail(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(NotFoundSessionException::new);

        return SessionDetailResponseDto.of(session);
    }
}
