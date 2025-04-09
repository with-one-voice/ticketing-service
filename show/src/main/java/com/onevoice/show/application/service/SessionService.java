package com.onevoice.show.application.service;

import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    CreateSessionResponseDto create(UUID showId, CreateSessionRequestDto requestDto);

    List<SessionResponseDto> getAllSessions();

    List<SessionResponseDto> getShowSessions(UUID showId);

    SessionResponseDto getOneSession(UUID sessionId);
}
