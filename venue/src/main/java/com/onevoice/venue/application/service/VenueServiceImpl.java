package com.onevoice.venue.application.service;

import org.springframework.stereotype.Service;

import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.exception.DuplicateVenueException;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;

import lombok.RequiredArgsConstructor;

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

		Venue saved = venueRepository.save(venue);

		return CreateVenueResponseDto.of(saved);
	}
}
