package com.onevoice.show.application.service;

import com.onevoice.show.application.dto.FindShowQuery;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.DuplicateShowException;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    @Override
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
}
