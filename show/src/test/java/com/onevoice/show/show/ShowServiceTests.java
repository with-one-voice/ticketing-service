package com.onevoice.show.show;

import com.onevoice.show.application.client.VenueClient;
import com.onevoice.show.application.dto.VenueResponseDto;
import com.onevoice.show.application.service.ShowServiceImpl;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.exception.InvalidVenueIdException;
import com.onevoice.show.exception.NotFoundShowException;
import com.onevoice.show.infrastructure.redis.RedisService;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import java.time.LocalDateTime;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShowServiceTests {

    @InjectMocks
    private ShowServiceImpl showServiceImpl;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private VenueClient venueClient;

    @Mock
    private RedisService redisService;

    @Test
    @DisplayName("공연 등록 성공")
    void getAllShows() {
        // given
        UUID venueId = UUID.randomUUID();
        CreateShowRequestDto requestDto = new CreateShowRequestDto(
            venueId,
            "테스트 공연",
            "가수 이름",
            ShowCategory.CONCERT,
            "http://image.url/poster.png",
            "설명",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );

        // 공연 제목 중복 없음
        when(showRepository.findByTitle(requestDto.title())).thenReturn(Optional.empty());

        // 유효한 공연장 응답
        when(venueClient.getVenueOne(venueId)).thenReturn(Optional.of(
            new VenueResponseDto(venueId, "공연장", "서울", "설명", 100)
        ));

        // 저장 시 객체 반환
        Show savedShow = Show.builder()
            .id(UUID.randomUUID())
            .venueId(requestDto.venueId())
            .title(requestDto.title())
            .artist(requestDto.artist())
            .category(requestDto.category())
            .posterUrl(requestDto.posterUrl())
            .description(requestDto.description())
            .ticketingStartTime(requestDto.ticketingStartTime())
            .ticketingEndTime(requestDto.ticketingEndTime())
            .status(Status.BEFORE)
            .build(); // toEntity 함수 필요시 구현
        when(showRepository.save(any())).thenReturn(savedShow);

        // when
        CreateShowResponseDto response = showServiceImpl.create(requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("테스트 공연");
        verify(venueClient).getVenueOne(venueId);
        verify(showRepository).save(any());
    }

    @Test
    @DisplayName("공연 등록 실패 - 존재하지 않는 공연장 ID")
    void createShowFail_InvalidVenue() {
        // given
        UUID invalidVenueId = UUID.randomUUID();
        CreateShowRequestDto requestDto = new CreateShowRequestDto(
            invalidVenueId,
            "테스트 공연",
            "가수",
            ShowCategory.CONCERT,
            "url",
            "설명",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2)
        );

        // 공연 제목 중복 없음
        when(showRepository.findByTitle(requestDto.title())).thenReturn(Optional.empty());

        // venueClient가 empty 반환
        when(venueClient.getVenueOne(invalidVenueId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> showServiceImpl.create(requestDto))
            .isInstanceOf(InvalidVenueIdException.class);

        verify(venueClient).getVenueOne(invalidVenueId);
        verify(showRepository, never()).save(any());
    }

    @Test
    @DisplayName("공연 단일 조회 성공")
    void getOneShowSuccess() {
        // given
        UUID showId = UUID.randomUUID();
        Show show = Show.builder()
            .id(showId)
            .title("테스트 공연")
            .artist("아티스트")
            .category(ShowCategory.CONCERT)
            .description("설명")
            .posterUrl("url")
            .ticketingStartTime(LocalDateTime.now().plusHours(1))
            .ticketingEndTime(LocalDateTime.now().plusHours(2))
            .venueId(UUID.randomUUID())
            .status(Status.OPEN)
            .build();

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));

        // when
        ShowResponseDto responseDto = showServiceImpl.getOne(showId);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.title()).isEqualTo("테스트 공연");
        verify(redisService).increaseShowViewCount(showId);
    }

    @Test
    @DisplayName("공연 단일 조회 실패 - 존재하지 않는 공연")
    void getOneShowFail_NotFound() {
        // given
        UUID showId = UUID.randomUUID();
        when(showRepository.findById(showId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> showServiceImpl.getOne(showId))
            .isInstanceOf(NotFoundShowException.class);

        verify(redisService, never()).increaseShowViewCount(any());
    }

    @Test
    @DisplayName("공연 전체 목록 조회")
    void getAllShowsSuccess() {
        // given
        List<Show> showList = List.of(
            Show.builder()
                .id(UUID.randomUUID())
                .title("공연1")
                .artist("가수1")
                .category(ShowCategory.CONCERT)
                .description("설명")
                .posterUrl("url1")
                .ticketingStartTime(LocalDateTime.now().plusHours(1))
                .ticketingEndTime(LocalDateTime.now().plusHours(2))
                .venueId(UUID.randomUUID())
                .status(Status.OPEN)
                .build(),
            Show.builder()
                .id(UUID.randomUUID())
                .title("공연2")
                .artist("가수2")
                .category(ShowCategory.MUSICAL)
                .description("설명2")
                .posterUrl("url2")
                .ticketingStartTime(LocalDateTime.now().plusHours(1))
                .ticketingEndTime(LocalDateTime.now().plusHours(2))
                .venueId(UUID.randomUUID())
                .status(Status.OPEN)
                .build()
        );

        when(showRepository.findAll()).thenReturn(showList);

        // when
        List<ShowResponseDto> responseDtoList = showServiceImpl.getAll();

        // then
        assertThat(responseDtoList.size()).isEqualTo(2);
        assertThat(responseDtoList.get(0).title()).isEqualTo("공연1");
        assertThat(responseDtoList.get(1).title()).isEqualTo("공연2");
    }

}
