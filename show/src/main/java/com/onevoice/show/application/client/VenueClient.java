package com.onevoice.show.application.client;

import com.onevoice.show.application.dto.client.VenueResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venue-service")
public interface VenueClient {

    @GetMapping("/internal/{venueId}")
    Optional<VenueResponseDto> getVenueOne(@PathVariable("venueId") UUID venueId);

}
