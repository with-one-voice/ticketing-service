package com.onevoice.show.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.service.ShowService;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateShowResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateShowResponseDto>> create(
        @RequestBody CreateShowRequestDto requestDto
    ) {

        CreateShowResponseDto responseDto = showService.create(requestDto);
        return CommonResponse.success(responseDto);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<CommonResponse<ShowResponseDto>> getOne(
        @PathVariable("showId") UUID showId
    ) {

        ShowResponseDto responseDto = showService.getOne(showId);
        return CommonResponse.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ShowResponseDto>>> getAll() {

        List<ShowResponseDto> responseDtoList = showService.getAll();
        return CommonResponse.success(responseDtoList);
    }

    @PatchMapping("/{showId}")
    public ResponseEntity<CommonResponse<UpdateShowResponseDto>> update(
        @PathVariable("showId") UUID showId,
        @RequestBody UpdateShowRequestDto requestDto
    ) {

        UpdateShowResponseDto responseDto = showService.update(showId, requestDto);
        return CommonResponse.success(responseDto);
    }

}
