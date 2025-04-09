package com.onevoice.show.application.service;

import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import java.util.UUID;

public interface SessionService {

    CreateSessionResponseDto create(UUID showId, CreateSessionRequestDto requestDto);
}
