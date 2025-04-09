package com.onevoice.show.application.service;

import com.onevoice.show.application.dto.FindSessionQuery;
import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.DuplicateSessionException;
import com.onevoice.show.exception.NotFoundSessionException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.exception.TicketingAlreadyStartedException;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
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

    @Override
    @Transactional
    public CreateSessionResponseDto create(UUID showId, CreateSessionRequestDto requestDto) {

        Show show = showRepository.findById(showId).orElseThrow(NotFoundShowException::new);

        //TODO: 공연 회차 등록 유효성 검사

        Session session = Session.builder()
            .show(show)
            .sessionDate(requestDto.sessionDate())
            .startTime(requestDto.startTime())
            .endTime(requestDto.endTime())
            .seatCount(requestDto.seatCount())
            .seatPrice(requestDto.seatPrice())
            .build();

        FindSessionQuery query = FindSessionQuery.of(sessionRepository.save(session));

        //TODO: 좌석 생성 FeignClient 호출

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

        //TODO: 회차 정보 수정 유효성 검사 보완

        // 해당 회차에 관한 정보가 이미 있는 경우 -> 공연 회차 정보 수정 불가
        if (sessionRepository.find(session.getShow().getId(), requestDto.sessionDate())
            .isPresent()) {
            throw new DuplicateSessionException();
        }

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
}
