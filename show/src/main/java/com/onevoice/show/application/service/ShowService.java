package com.onevoice.show.application.service;

import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.request.UpdateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import com.onevoice.show.presentation.dto.response.UpdateShowResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowService {

    CreateShowResponseDto create(CreateShowRequestDto requestDto);

    ShowResponseDto getOne(UUID showId);

    List<ShowResponseDto> getAll();

    UpdateShowResponseDto update(UUID showId, UpdateShowRequestDto requestDto);

    void delete(UUID showId, UUID userId);

    Page<ShowResponseDto> search(String keyword, Pageable pageable);

    void updateStatus(UUID showId);

    List<ShowResponseDto> getTop5ViewedShows();
}
