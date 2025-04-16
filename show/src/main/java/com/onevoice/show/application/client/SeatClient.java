package com.onevoice.show.application.client;

import com.onevoice.show.application.dto.client.SeatCreateRequestDto;
import com.onevoice.show.application.dto.client.SeatCreateResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seat-service")
public interface SeatClient {

    @PostMapping("/internal/create")
    Optional<List<SeatCreateResponseDto>> createInternal(
        @RequestBody @Valid SeatCreateRequestDto request);
}
