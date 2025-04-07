package com.onevoice.venue.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.venue.application.service.VenueService;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{venueId}")
    public ResponseEntity<CommonResponse<VenueResponseDto>> getOne(
        @PathVariable("venueId") UUID venueId) {

        VenueResponseDto responseDto = venueService.getOne(venueId);
        return CommonResponse.success(responseDto);
    }

}
