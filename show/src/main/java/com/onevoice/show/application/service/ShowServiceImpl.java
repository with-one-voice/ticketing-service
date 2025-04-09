package com.onevoice.show.application.service;

import com.onevoice.show.application.dto.FindShowQuery;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.DuplicateShowException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.exception.TicketingAlreadyStartedException;
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

    @Override
    @Transactional
    public CreateShowResponseDto create(CreateShowRequestDto requestDto) {

        if (showRepository.findByTitle(requestDto.title()).isPresent()) {
            throw new DuplicateShowException();
        }

        Show show = Show.builder()
            .venueId(requestDto.venueId())
            .title(requestDto.title())
            .artist(requestDto.artist())
            .category(requestDto.category())
            .posterUrl(requestDto.posterUrl())
            .description(requestDto.description())
            .ticketingStartTime(requestDto.ticketingStartTime())
            .ticketingEndTime(requestDto.ticketingEndTime())
            .build();

        FindShowQuery query = FindShowQuery.of(showRepository.save(show));

        return CreateShowResponseDto.of(query);
    }

    @Override
    public ShowResponseDto getOne(UUID showId) {

        FindShowQuery query = FindShowQuery.of(showRepository.findById(showId)
            .orElseThrow(NotFoundShowException::new));

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
}
