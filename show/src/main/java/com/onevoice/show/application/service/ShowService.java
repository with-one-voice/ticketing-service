package com.onevoice.show.application.service;

import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;
import com.onevoice.show.presentation.dto.response.ShowResponseDto;
import java.util.List;
import java.util.UUID;

public interface ShowService {

    CreateShowResponseDto create(CreateShowRequestDto requestDto);

    ShowResponseDto getOne(UUID showId);

    List<ShowResponseDto> getAll();
}
