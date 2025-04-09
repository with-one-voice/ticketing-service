package com.onevoice.show.application.service;

import com.onevoice.show.application.dto.FindSessionQuery;
import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
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

        Session session = Session.builder()
            .show(show)
            .sessionDate(requestDto.sessionDate())
            .startTime(requestDto.startTime())
            .endTime(requestDto.endTime())
            .seatCount(requestDto.seatCount())
            .seatPrice(requestDto.seatPrice())
            .build();

        FindSessionQuery query = FindSessionQuery.of(sessionRepository.save(session));

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

        List<FindSessionQuery> queries = sessionRepository.findByShowId(showId).stream()
            .map(FindSessionQuery::of).toList();

        return queries.stream().map(SessionResponseDto::of).toList();
    }

    @Override
    public SessionResponseDto getOneSession(UUID sessionId) {

        FindSessionQuery query = FindSessionQuery.of(
            sessionRepository.findById(sessionId).orElseThrow(NotFoundShowException::new));

        return SessionResponseDto.of(query);
    }
}
