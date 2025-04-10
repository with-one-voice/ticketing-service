package com.onevoice.venue.presentation;

import com.onevoice.venue.application.service.VenueService;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class VenueInternalController {

    private final VenueService venueService;

    @GetMapping("/{venueId}")
    public Optional<VenueResponseDto> getVenueOne(
        @PathVariable("venueId") UUID venueId
    ) {

        VenueResponseDto responseDto = venueService.getOne(venueId);
        return Optional.ofNullable(responseDto);
    }

}
