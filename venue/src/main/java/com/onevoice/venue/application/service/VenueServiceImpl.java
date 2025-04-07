package com.onevoice.venue.application.service;

import com.onevoice.venue.application.dto.FindVenueQuery;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.exception.DuplicateVenueException;
import com.onevoice.venue.exception.NotFoundVenueException;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public CreateVenueResponseDto create(CreateVenueRequestDto requestDto) {

        if (venueRepository.findByName(requestDto.name()).isPresent()) {
            throw new DuplicateVenueException();
        }

        Venue venue = Venue.builder()
            .name(requestDto.name())
            .location(requestDto.location())
            .description(requestDto.description())
            .totalSeatCount(requestDto.totalSeatCount())
            .build();

        FindVenueQuery query = FindVenueQuery.of(venueRepository.save(venue));

        return CreateVenueResponseDto.of(query);
    }

    @Override
    public VenueResponseDto getOne(UUID venueId) {

        Venue venue = venueRepository.findById(venueId).orElseThrow(NotFoundVenueException::new);

        FindVenueQuery query = FindVenueQuery.of(venue);

        return VenueResponseDto.of(query);
    }
}
