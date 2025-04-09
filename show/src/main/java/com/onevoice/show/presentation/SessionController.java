package com.onevoice.show.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.show.application.service.SessionService;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/{showId}")
    public ResponseEntity<CommonResponse<CreateSessionResponseDto>> create(
        @PathVariable("showId") UUID showId,
        @RequestBody CreateSessionRequestDto requestDto
    ) {

        CreateSessionResponseDto responseDto = sessionService.create(showId, requestDto);
        return CommonResponse.success(responseDto);
    }

}
