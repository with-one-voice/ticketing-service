package com.onevoice.show.application.service.session;

import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionDetailResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateSessionResponseDto;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    CreateSessionResponseDto create(UUID showId, CreateSessionRequestDto requestDto);

    List<SessionResponseDto> getAllSessions();

    List<SessionResponseDto> getShowSessions(UUID showId);

    SessionResponseDto getOneSession(UUID sessionId);

    UpdateSessionResponseDto update(UUID sessionId, UpdateSessionRequestDto requestDto);

    void delete(UUID sessionId, UUID userId);

    void updateStatus(UUID sessionId);

    SessionDetailResponseDto getSessionDetail(UUID sessionId);
}
