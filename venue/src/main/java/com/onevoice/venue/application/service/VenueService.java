package com.onevoice.venue.application.service;

import com.onevoice.venue.application.dto.FindVenueQuery;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.request.UpdateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.UpdateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VenueService {

    CreateVenueResponseDto create(CreateVenueRequestDto requestDto);

    VenueResponseDto getOne(UUID venueId);

    List<VenueResponseDto> getAll();

    UpdateVenueResponseDto update(UUID venueId, UpdateVenueRequestDto requestDto);

    void delete(UUID venueId, UUID userId);

    Page<VenueResponseDto> search(String keyword, Pageable pageable);

    Optional<FindVenueQuery> getOneInternal(UUID venueId);
}
