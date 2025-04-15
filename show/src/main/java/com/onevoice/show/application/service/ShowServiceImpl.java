package com.onevoice.show.application.service;

import com.onevoice.show.application.client.VenueClient;
import com.onevoice.show.application.dto.query.FindShowQuery;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.Status;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.DuplicateShowException;
import com.onevoice.show.exception.InvalidTicketingDateException;
import com.onevoice.show.exception.InvalidVenueIdException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.exception.TicketingAlreadyStartedException;
import com.onevoice.show.infrastructure.redis.RedisService;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateShowResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final VenueClient venueClient;
    private final RedisService redisService;

    @Override
    @Transactional
    public CreateShowResponseDto create(CreateShowRequestDto requestDto) {

        if (showRepository.findByTitle(requestDto.title()).isPresent()) {
            throw new DuplicateShowException();
        }

        // 공연장 정보 확인
        if (venueClient.getVenueOne(requestDto.venueId()).isEmpty()) {
            throw new InvalidVenueIdException();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = requestDto.ticketingStartTime();
        LocalDateTime end = requestDto.ticketingEndTime();

        // 티켓 예매 날짜 유효성 검사
        if (start.isBefore(now) || !start.isBefore(end) || end.isBefore(now)) {
            throw new InvalidTicketingDateException();
        }

        Show show = Show.builder()
            .venueId(requestDto.venueId())
            .title(requestDto.title())
            .artist(requestDto.artist())
            .category(requestDto.category())
            .posterUrl(requestDto.posterUrl())
            .description(requestDto.description())
            .ticketingStartTime(start)
            .ticketingEndTime(end)
            .status(Status.BEFORE)
            .build();

        FindShowQuery query = FindShowQuery.of(showRepository.save(show));

        return CreateShowResponseDto.of(query);
    }

    @Override
    public ShowResponseDto getOne(UUID showId) {

        FindShowQuery query = FindShowQuery.of(showRepository.findById(showId)
            .orElseThrow(NotFoundShowException::new));

        // 조회수 증가
        redisService.increaseShowViewCount(showId);

        return ShowResponseDto.of(query);
    }

    @Override
    public List<ShowResponseDto> getAll() {

        List<FindShowQuery> showQueryList = showRepository.findAll().stream()
            .map(FindShowQuery::of).toList();

        return showQueryList.stream().map(ShowResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpdateShowResponseDto update(UUID showId, UpdateShowRequestDto requestDto) {

        Show show = showRepository.findById(showId)
            .orElseThrow(NotFoundShowException::new);

        if (!show.getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        show.update(requestDto);

        FindShowQuery query = FindShowQuery.of(show);

        return UpdateShowResponseDto.of(query);
    }

    @Override
    @Transactional
    public void delete(UUID showId, UUID userId) {

        // TODO: 회차 정보 확인 후, 존재하는 경우 삭제 불가 OR 티켓팅 전이라면 좌석, 회차, 공연 순으로 삭제

        Show show = showRepository.findById(showId)
            .orElseThrow(NotFoundShowException::new);

        if (!show.getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        show.delete(userId);

    }

    @Override
    public Page<ShowResponseDto> search(String keyword, Pageable pageable) {

        List<FindShowQuery> queries = showRepository.search(keyword, pageable)
            .stream().map(FindShowQuery::of).toList();

        Long total = showRepository.getTotal(keyword);

        List<ShowResponseDto> dtoList = queries.stream()
            .map(ShowResponseDto::of)
            .toList();

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    @Transactional
    public void updateStatus(UUID showId) {
        Show show = showRepository.findById(showId)
            .orElseThrow(NotFoundShowException::new);

        // 티켓팅이 이미 진행되었다면, 공연 상태 변경 불가
        if (!show.getTicketingStartTime().isAfter(LocalDateTime.now())) {
            throw new TicketingAlreadyStartedException();
        }

        show.updateStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponseDto> getTop5ViewedShows() {
        //TODO:조회수 상위권 5개 공연 조회 로직
        return List.of();
    }
}
