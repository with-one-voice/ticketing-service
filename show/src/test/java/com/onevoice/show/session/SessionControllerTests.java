package com.onevoice.show.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.application.service.SessionService;
import com.onevoice.show.domain.Status;
import com.onevoice.show.presentation.SessionController;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SessionController.class)
public class SessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공연 회차 등록 성공")
    void createSessionSuccess() throws Exception {
        // given
        UUID showId = UUID.randomUUID();
        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now().plusDays(5),
            LocalTime.of(18, 0),
            LocalTime.of(20, 0),
            50,
            1000L
        );

        CreateSessionResponseDto response = new CreateSessionResponseDto(
            UUID.randomUUID(), showId, request.sessionDate(), request.startTime(),
            request.endTime(), request.seatCount(), request.seatPrice(), Status.BEFORE
        );

        when(sessionService.create(eq(showId), any(CreateSessionRequestDto.class))).thenReturn(
            response);

        // when & then
        mockMvc.perform(post("/sessions/" + showId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.sessionDate").value(request.sessionDate().toString()));
    }

    @Test
    @DisplayName("공연 회차 목록 조회 성공")
    void getShowSessionsSuccess() throws Exception {
        UUID showId = UUID.randomUUID();

        List<SessionResponseDto> sessions = List.of(
            new SessionResponseDto(UUID.randomUUID(), showId, LocalDate.now().plusDays(5),
                LocalTime.of(18, 0), LocalTime.of(20, 0), 50, 1000L, Status.BEFORE,
                LocalDateTime.now(), UUID.randomUUID()),
            new SessionResponseDto(UUID.randomUUID(), showId, LocalDate.now().plusDays(6),
                LocalTime.of(18, 0), LocalTime.of(20, 0), 60, 1200L, Status.BEFORE,
                LocalDateTime.now(), UUID.randomUUID())
        );

        when(sessionService.getShowSessions(showId)).thenReturn(sessions);

        mockMvc.perform(get("/" + showId + "/sessions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.length()").value(2));
    }

}
