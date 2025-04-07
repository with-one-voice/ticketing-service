package com.onevoice.venue.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.venue.application.service.VenueService;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VenueController {
	private final VenueService venueService;

	@PostMapping
	public ResponseEntity<CommonResponse<CreateVenueResponseDto>> create(
		@RequestBody CreateVenueRequestDto requestDto) {

		CreateVenueResponseDto responseDto = venueService.create(requestDto);
		return CommonResponse.success(responseDto);
	}
}
