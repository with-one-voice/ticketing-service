package com.onevoice.venue.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.venue.application.service.VenueService;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;
import com.onevoice.venue.presentation.dto.request.UpdateVenueRequestDto;
import com.onevoice.venue.presentation.dto.response.CreateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.UpdateVenueResponseDto;
import com.onevoice.venue.presentation.dto.response.VenueResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateVenueResponseDto>> create(
        @RequestBody CreateVenueRequestDto requestDto) {

        CreateVenueResponseDto responseDto = venueService.create(requestDto);
        return CommonResponse.success(responseDto);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<CommonResponse<VenueResponseDto>> getOne(
        @PathVariable("venueId") UUID venueId) {

        VenueResponseDto responseDto = venueService.getOne(venueId);
        return CommonResponse.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<VenueResponseDto>>> getAll() {

        List<VenueResponseDto> responseDtoList = venueService.getAll();
        return CommonResponse.success(responseDtoList);
    }

    @PatchMapping("/{venueId}")
    public ResponseEntity<CommonResponse<UpdateVenueResponseDto>> update(
        @PathVariable("venueId") UUID venueId,
        @RequestBody UpdateVenueRequestDto requestDto) {

        UpdateVenueResponseDto responseDto = venueService.update(venueId, requestDto);
        return CommonResponse.success(responseDto);
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<CommonResponse<String>> delete(
        @PathVariable("venueId") UUID venueId,
        @AuthenticationPrincipal UUID userId
    ) {

        venueService.delete(venueId, userId);
        return CommonResponse.success("공연장 삭제가 완료되었습니다.");
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<Page<VenueResponseDto>>> search(
        @RequestParam(name = "keyword") String keyword,
        @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
    ) {

        Page<VenueResponseDto> responseDtoList = venueService.search(keyword, pageable);
        return CommonResponse.success(responseDtoList);
    }

}
