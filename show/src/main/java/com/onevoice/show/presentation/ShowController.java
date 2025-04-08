package com.onevoice.show.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.service.ShowService;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

}
