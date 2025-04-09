package com.onevoice.show.application.client;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.dto.VenueResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venue-service")
public interface VenueClient {

    @GetMapping("/{venueId}")
    ResponseEntity<CommonResponse<VenueResponseDto>> getOne(@PathVariable("venueId") UUID venueId);

}
