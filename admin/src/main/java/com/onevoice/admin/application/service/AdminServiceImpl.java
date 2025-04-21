package com.onevoice.admin.application.service;

import com.onevoice.admin.application.client.ShowClient;
import com.onevoice.admin.presentation.dto.client.AdminCreateSessionRequest;
import com.onevoice.admin.application.dto.AdminCreateSessionResponse;
import com.onevoice.admin.presentation.dto.client.AdminCreateShowRequest;
import com.onevoice.admin.application.dto.AdminCreateShowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ShowClient showClient;

    @Override
    public Optional<AdminCreateShowResponse> createShow(AdminCreateShowRequest request) {
        return showClient.createShow(request);
    }
    @Override
    public Optional<AdminCreateSessionResponse> createSession(UUID showId, AdminCreateSessionRequest request, UUID userId) {
        return showClient.createSession(showId, request, userId);
    }

}
