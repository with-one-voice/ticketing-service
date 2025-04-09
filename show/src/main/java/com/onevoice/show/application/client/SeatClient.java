package com.onevoice.show.application.client;

import com.onevoice.show.application.dto.CreateSeatRequestDto;
import com.onevoice.show.application.dto.SeatResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seat-service")
public interface SeatClient {

    @PostMapping("/internal/create")
    List<SeatResponseDto> create(@RequestBody CreateSeatRequestDto request);
}
