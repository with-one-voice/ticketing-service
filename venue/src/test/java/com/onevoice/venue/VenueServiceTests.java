package com.onevoice.venue;

import com.onevoice.venue.application.service.VenueServiceImpl;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.exception.DuplicateVenueException;
import com.onevoice.venue.exception.NotFoundVenueException;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class VenueServiceTests {

    @InjectMocks
    private VenueServiceImpl venueServiceImpl;

    @Mock
    private VenueRepository venueRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("공연장 등록 성공")
    void createVenueSuccess() {
        // given
        CreateVenueRequestDto requestDto = new CreateVenueRequestDto("테스트 공연장", "서울시 상암동",
            "테스트용 공연장입니다.", 100);

        Venue venue = Venue.builder()
            .id(UUID.randomUUID())
            .name("테스트 공연장")
            .location("서울시 상암동")
            .description("테스트용 공연장입니다.")
            .totalSeatCount(100)
            .build();

        when(venueRepository.findByName(requestDto.name())).thenReturn(Optional.empty());
        when(venueRepository.save(any(Venue.class))).thenReturn(venue);

        // when
        CreateVenueResponseDto response = venueServiceImpl.create(requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("테스트 공연장");
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    @DisplayName("공연장 등록 실패 - 공연장 이름 중복")
    void createVenueFail_Duplicate() {
        // given
        CreateVenueRequestDto requestDto = new CreateVenueRequestDto(
            "테스트 공연장", "서울시 장충동", "테스트용 공연장입니다.", 200
        );

        Venue duplicatedVenue = Venue.builder()
            .id(UUID.randomUUID())
            .name("테스트 공연장")
            .location("서울시 장충동")
            .description("테스트용 공연장입니다.")
            .totalSeatCount(200)
            .build();

        // mock: 이름 중복인 경우 Optional에 값이 있음
        when(venueRepository.findByName(requestDto.name())).thenReturn(
            Optional.of(duplicatedVenue));

        // when & then
        assertThatThrownBy(() -> venueServiceImpl.create(requestDto))
            .isInstanceOf(DuplicateVenueException.class);
    }

    @Test
    @DisplayName("공연장 단일 조회 성공")
    void getOneVenueSuccess() {
        // given
        UUID venueId = UUID.randomUUID();
        Venue venue = Venue.builder()
            .id(venueId)
            .name("테스트 공연장")
            .location("서울")
            .description("테스트 설명")
            .totalSeatCount(200)
            .build();

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));

        // when
        VenueResponseDto response = venueServiceImpl.getOne(venueId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.venueId()).isEqualTo(venueId);
        assertThat(response.name()).isEqualTo("테스트 공연장");
    }

    @Test
    @DisplayName("공연장 단일 조회 실패 - 존재하지 않는 공연장 ID")
    void getOneVenueFail_NotFound() {
        // given
        UUID venueId = UUID.randomUUID();

        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> venueServiceImpl.getOne(venueId))
            .isInstanceOf(NotFoundVenueException.class);
    }

    @Test
    @DisplayName("공연장 전체 조회 성공")
    void getAllVenuesSuccess() {
        // given
        List<Venue> venues = List.of(
            Venue.builder()
                .id(UUID.randomUUID())
                .name("공연장 A")
                .location("서울")
                .description("A 설명")
                .totalSeatCount(100)
                .build(),
            Venue.builder()
                .id(UUID.randomUUID())
                .name("공연장 B")
                .location("부산")
                .description("B 설명")
                .totalSeatCount(150)
                .build()
        );

        when(venueRepository.findAll()).thenReturn(venues);

        // when
        List<VenueResponseDto> result = venueServiceImpl.getAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).name()).isEqualTo("공연장 A");
        assertThat(result.get(1).name()).isEqualTo("공연장 B");
    }

}
