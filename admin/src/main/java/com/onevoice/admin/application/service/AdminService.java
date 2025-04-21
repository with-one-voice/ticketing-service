package com.onevoice.admin.application.service;

import com.onevoice.admin.presentation.dto.client.AdminCreateSessionRequest;
import com.onevoice.admin.application.dto.AdminCreateSessionResponse;
import com.onevoice.admin.presentation.dto.client.AdminCreateShowRequest;
import com.onevoice.admin.application.dto.AdminCreateShowResponse;

import java.util.Optional;
import java.util.UUID;

public interface AdminService {
    Optional<AdminCreateShowResponse> createShow(AdminCreateShowRequest request);
    Optional<AdminCreateSessionResponse> createSession(UUID showId, AdminCreateSessionRequest request,UUID userId);


}