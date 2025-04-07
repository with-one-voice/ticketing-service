package com.onevoice.venue.application.service;

import com.onevoice.venue.application.dto.FindVenueQuery;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.exception.DuplicateVenueException;
import com.onevoice.venue.exception.NotFoundVenueException;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.request.UpdateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.UpdateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    @Transactional
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

    @Override
    public List<VenueResponseDto> getAll() {

        List<FindVenueQuery> venueQueryList = venueRepository.findAll().stream()
            .map(FindVenueQuery::of).toList();

        return venueQueryList.stream().map(VenueResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpdateVenueResponseDto update(UUID venueId, UpdateVenueRequestDto requestDto) {

        Venue venue = venueRepository.findById(venueId).orElseThrow(NotFoundVenueException::new);

        venue.update(requestDto);

        FindVenueQuery query = FindVenueQuery.of(venue);

        return UpdateVenueResponseDto.of(query);
    }

    @Override
    @Transactional
    public void delete(UUID venueId) {

        Venue venue = venueRepository.findById(venueId).orElseThrow(NotFoundVenueException::new);

        //TODO : security 적용 후 현재 로그인한 사용자 userId 넣기
        venue.delete(UUID.randomUUID());
    }
}
