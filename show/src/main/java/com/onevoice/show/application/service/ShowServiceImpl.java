package com.onevoice.show.application.service;

import com.onevoice.show.application.dto.FindShowQuery;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.DuplicateShowException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
}
