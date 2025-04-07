package com.onevoice.venue.application.service;

import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;

public interface VenueService {

	CreateVenueResponseDto create(CreateVenueRequestDto requestDto);


}
