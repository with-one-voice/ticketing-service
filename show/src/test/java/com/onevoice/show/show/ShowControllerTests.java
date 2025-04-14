package com.onevoice.show.show;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.application.service.ShowService;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import com.onevoice.show.presentation.ShowController;
import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
@WebMvcTest(controllers = ShowController.class)
class ShowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowService showService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공연 등록 성공")
    void createShowSuccess() throws Exception {
        CreateShowRequestDto requestDto = new CreateShowRequestDto(
            UUID.randomUUID(), "테스트 공연", "아티스트", ShowCategory.CONCERT,
            "https://image.com/poster.jpg", "설명입니다.",
            LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        );

        CreateShowResponseDto responseDto = new CreateShowResponseDto(
            UUID.randomUUID(), requestDto.venueId(), requestDto.title(), requestDto.artist(),
            requestDto.category(),
            requestDto.posterUrl(), requestDto.description(),
            requestDto.ticketingStartTime(), requestDto.ticketingEndTime(), Status.BEFORE
        );

        Mockito.when(showService.create(any())).thenReturn(responseDto);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title", is("테스트 공연")))
            .andExpect(jsonPath("$.result.artist", is("아티스트")));
    }

    @Test
    @DisplayName("공연 단일 조회 성공")
    void getOneShowSuccess() throws Exception {
        UUID showId = UUID.randomUUID();
        ShowResponseDto responseDto = new ShowResponseDto(
            showId, UUID.randomUUID(), "테스트 공연", "아티스트", ShowCategory.CONCERT,
            "https://poster.url", "공연 설명",
            LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), Status.BEFORE,
            LocalDateTime.now(), UUID.randomUUID()
        );

        Mockito.when(showService.getOne(showId)).thenReturn(responseDto);

        mockMvc.perform(get("/" + showId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.title", is("테스트 공연")))
            .andExpect(jsonPath("$.result.showId", is(showId.toString())));
    }

    @Test
    @DisplayName("공연 전체 조회 성공")
    void getAllShowsSuccess() throws Exception {
        UUID venueId = UUID.randomUUID();
        List<ShowResponseDto> showList = List.of(
            new ShowResponseDto(UUID.randomUUID(), venueId, "공연1", "가수1", ShowCategory.CONCERT,
                "url", "설명", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                Status.BEFORE, LocalDateTime.now(), UUID.randomUUID()),
            new ShowResponseDto(UUID.randomUUID(), venueId, "공연2", "가수1", ShowCategory.CONCERT,
                "url", "설명", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4),
                Status.BEFORE, LocalDateTime.now(), UUID.randomUUID())
        );

        Mockito.when(showService.getAll()).thenReturn(showList);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.length()", is(2)))
            .andExpect(jsonPath("$.result[0].title", is("공연1")))
            .andExpect(jsonPath("$.result[1].title", is("공연2")));
    }
}
