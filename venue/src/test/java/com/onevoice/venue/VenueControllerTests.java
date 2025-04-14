package com.onevoice.venue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.venue.application.service.VenueService;
import com.onevoice.venue.exception.DuplicateVenueException;
import com.onevoice.venue.exception.NotFoundVenueException;
import com.onevoice.venue.presentation.VenueController;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
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
@WebMvcTest(controllers = VenueController.class)
public class VenueControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공연장 등록 성공")
    void createVenue() throws Exception {
        // given
        CreateVenueRequestDto requestDto = new CreateVenueRequestDto("테스트 공연장", "서울시 상암동", "설명",
            100);
        CreateVenueResponseDto responseDto = new CreateVenueResponseDto(UUID.randomUUID(),
            "테스트 공연장", "서울시 상암동", "설명", 100);

        Mockito.when(venueService.create(any())).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.name", is("테스트 공연장")));
    }

    @Test
    @DisplayName("공연장 등록 실패 - 공연장 이름 중복")
    void createVenueFail_Duplicate() throws Exception {
        // given
        CreateVenueRequestDto requestDto = new CreateVenueRequestDto(
            "테스트 공연장", "서울시 상암동", "설명", 100
        );

        Mockito.when(venueService.create(any()))
            .thenThrow(new DuplicateVenueException());

        // when & then
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isConflict()) // 409 CONFLICT
            .andExpect(jsonPath("$.code", is(4490))) // ResponseCode.DUPLICATE_VENUE
            .andExpect(jsonPath("$.message", containsString("이미 존재하는 공연장")));
    }

    @Test
    @DisplayName("공연장 단일 조회 성공")
    void getOneVenue() throws Exception {
        // given
        UUID venueId = UUID.randomUUID();
        VenueResponseDto responseDto = new VenueResponseDto(venueId, "테스트 공연장", "서울시", "설명", 100);

        Mockito.when(venueService.getOne(venueId)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(get("/" + venueId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.name", is("테스트 공연장")))
            .andExpect(jsonPath("$.result.venueId", is(venueId.toString())));
    }

    @Test
    @DisplayName("공연장 단일 조회 실패 - 공연장 없음")
    void getOneVenueFail_NotFound() throws Exception {
        // given
        UUID venueId = UUID.randomUUID();

        Mockito.when(venueService.getOne(venueId))
            .thenThrow(new NotFoundVenueException());

        // when & then
        mockMvc.perform(get("/" + venueId))
            .andExpect(status().isNotFound()) // 404 NOT FOUND
            .andExpect(jsonPath("$.code", is(4440))) // ResponseCode.VENUE_NOT_FOUND
            .andExpect(jsonPath("$.message", containsString("존재하지 않는 공연장")));
    }

    @Test
    @DisplayName("공연장 전체 조회 성공")
    void getAllVenues() throws Exception {
        // given
        List<VenueResponseDto> list = List.of(
            new VenueResponseDto(UUID.randomUUID(), "공연장1", "서울", "설명1", 100),
            new VenueResponseDto(UUID.randomUUID(), "공연장2", "부산", "설명2", 200)
        );

        Mockito.when(venueService.getAll()).thenReturn(list);

        // when & then
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", hasSize(2)))
            .andExpect(jsonPath("$.result[0].name", is("공연장1")))
            .andExpect(jsonPath("$.result[1].name", is("공연장2")));
    }

}
