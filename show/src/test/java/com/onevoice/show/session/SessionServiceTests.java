package com.onevoice.show.session;

import com.onevoice.show.application.client.SeatClient;
import com.onevoice.show.application.client.VenueClient;
import com.onevoice.show.application.dto.VenueResponseDto;
import com.onevoice.show.application.service.SessionServiceImpl;
import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.InvalidSeatCountException;
import com.onevoice.show.exception.InvalidSessionDateException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.exception.SeatCreateApiFailException;
import com.onevoice.show.presentation.dto.request.CreateSessionRequestDto;
import com.onevoice.show.presentation.dto.response.CreateSessionResponseDto;
import com.onevoice.show.presentation.dto.response.SessionResponseDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {

    @InjectMocks
    private SessionServiceImpl sessionServiceImpl;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private SeatClient seatClient;

    @Mock
    private VenueClient venueClient;

    @Test
    @DisplayName("공연 회차 등록 성공")
    void createSessionSuccess() {
        // given
        UUID showId = UUID.randomUUID();
        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusDays(2))
            .ticketingEndTime(LocalDateTime.now().plusDays(3))
            .venueId(UUID.randomUUID())
            .status(Status.BEFORE)
            .build();

        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now().plusDays(7), LocalTime.of(19, 0), LocalTime.of(21, 0), 50, 1000L
        );

        VenueResponseDto venueResponse = new VenueResponseDto(
            show.getVenueId(), "테스트 공연장", "서울", "설명", 100
        );

        Session savedSession = Session.builder()
            .id(UUID.randomUUID())
            .show(show)
            .sessionDate(request.sessionDate())
            .startTime(request.startTime())
            .endTime(request.endTime())
            .seatCount(request.seatCount())
            .seatPrice(request.seatPrice())
            .build();

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(sessionRepository.find(showId, request.sessionDate())).thenReturn(Optional.empty());
        when(venueClient.getVenueOne(show.getVenueId())).thenReturn(Optional.of(venueResponse));
        when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);
        when(seatClient.createInternal(any())).thenReturn(Optional.of(List.of()));

        // when
        CreateSessionResponseDto response = sessionServiceImpl.create(showId, request);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("공연 회차 등록 실패 - 존재하지 않는 공연 ID")
    void createSessionFail_NotFoundShow() {
        UUID invalidShowId = UUID.randomUUID();
        when(showRepository.findById(invalidShowId)).thenReturn(Optional.empty());

        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now(), LocalTime.of(19, 0), LocalTime.of(21, 0), 50, 10000L
        );

        assertThatThrownBy(() -> sessionServiceImpl.create(invalidShowId, request))
            .isInstanceOf(NotFoundShowException.class);
    }

    @Test
    @DisplayName("공연 회차 등록 실패 - 유효하지 않은 공연 회차 날짜")
    void createSessionFail_InvalidSessionDate() {
        UUID showId = UUID.randomUUID();
        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusDays(2))
            .ticketingEndTime(LocalDateTime.now().plusDays(3))
            .venueId(UUID.randomUUID())
            .status(Status.BEFORE)
            .build();

        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now().plusDays(1), LocalTime.of(19, 0), LocalTime.of(21, 0), 50,
            10000L
        );

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));

        assertThatThrownBy(() -> sessionServiceImpl.create(showId, request))
            .isInstanceOf(InvalidSessionDateException.class);
    }

    @Test
    @DisplayName("공연 회차 등록 실패 - 유효하지 않은 공연 회차 수용 인원")
    void createSessionFail_InvalidSessionSeatCount() {
        UUID showId = UUID.randomUUID();
        UUID venueId = UUID.randomUUID();

        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusDays(2))
            .ticketingEndTime(LocalDateTime.now().plusDays(3))
            .venueId(venueId)
            .status(Status.BEFORE)
            .build();

        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now().plusDays(7), LocalTime.of(19, 0), LocalTime.of(21, 0), 200, 10000L
        );

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(sessionRepository.find(showId, request.sessionDate())).thenReturn(Optional.empty());
        when(venueClient.getVenueOne(venueId)).thenReturn(Optional.of(
            new VenueResponseDto(venueId, "테스트", "서울", "설명", 100)
        ));

        assertThatThrownBy(() -> sessionServiceImpl.create(showId, request))
            .isInstanceOf(InvalidSeatCountException.class);
    }

    @Test
    @DisplayName("공연 회차 등록 실패 - 좌석 등록 API 호출 실패")
    void createSessionFail_FailToCreateSeat() {
        UUID showId = UUID.randomUUID();
        UUID venueId = UUID.randomUUID();

        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusDays(2))
            .ticketingEndTime(LocalDateTime.now().plusDays(3))
            .venueId(venueId)
            .status(Status.BEFORE)
            .build();

        CreateSessionRequestDto request = new CreateSessionRequestDto(
            LocalDate.now().plusDays(7), LocalTime.of(19, 0), LocalTime.of(21, 0), 50, 10000L
        );

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(sessionRepository.find(showId, request.sessionDate())).thenReturn(Optional.empty());
        when(venueClient.getVenueOne(venueId)).thenReturn(Optional.of(
            new VenueResponseDto(venueId, "테스트", "서울", "설명", 100)
        ));
        when(sessionRepository.save(any(Session.class))).thenAnswer(inv -> inv.getArgument(0));
        when(seatClient.createInternal(any())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> sessionServiceImpl.create(showId, request))
            .isInstanceOf(SeatCreateApiFailException.class);
    }

    @Test
    @DisplayName("공연에 해당하는 공연 회차 목록 조회 성공")
    void getAllSessionSuccess() {
        // given
        UUID showId = UUID.randomUUID();
        UUID venueId = UUID.randomUUID();

        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusDays(2))
            .ticketingEndTime(LocalDateTime.now().plusDays(3))
            .venueId(venueId)
            .status(Status.BEFORE)
            .build();

        List<Session> sessionList = List.of(
            Session.builder()
                .id(UUID.randomUUID())
                .show(show)
                .sessionDate(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(19, 0))
                .endTime(LocalTime.of(22, 0))
                .seatCount(50)
                .seatPrice(1000L)
                .build(),
            Session.builder()
                .id(UUID.randomUUID())
                .show(show)
                .sessionDate(LocalDate.now().plusDays(8))
                .startTime(LocalTime.of(19, 0))
                .endTime(LocalTime.of(22, 0))
                .seatCount(50)
                .seatPrice(1000L)
                .build()
        );

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(sessionRepository.findByShowId(showId)).thenReturn(sessionList);

        // when
        List<SessionResponseDto> result = sessionServiceImpl.getShowSessions(showId);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).sessionDate()).isEqualTo(sessionList.get(0).getSessionDate());
    }

    @Test
    @DisplayName("공연에 해당하는 공연 회차 목록 조회 실패 - 존재하지 않는 공연ID")
    void getAllSessionFail_InvalidSessionId() {
        UUID invalidId = UUID.randomUUID();
        when(showRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionServiceImpl.getShowSessions(invalidId))
            .isInstanceOf(NotFoundShowException.class);
    }
}
