package com.onevoice.show.application.service;

import com.onevoice.show.presentation.dto.request.CreateShowRequestDto;
import com.onevoice.show.presentation.dto.response.CreateShowResponseDto;

public interface ShowService {

    CreateShowResponseDto create(CreateShowRequestDto requestDto);
}
