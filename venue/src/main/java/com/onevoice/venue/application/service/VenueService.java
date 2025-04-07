package com.onevoice.venue.application.service;

import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.UUID;

public interface VenueService {

    CreateVenueResponseDto create(CreateVenueRequestDto requestDto);

    VenueResponseDto getOne(UUID venueId);

    List<VenueResponseDto> getAll();
}
