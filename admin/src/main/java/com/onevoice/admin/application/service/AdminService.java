package com.onevoice.admin.application.service;

import com.onevoice.admin.presentation.dto.client.session.*;
import com.onevoice.admin.presentation.dto.client.show.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminService {
    /*
     * show
     * */
    Optional<AdminCreateShowResponse> createShow(AdminCreateShowRequest request);
    Optional<AdminUpdateShowResponse> updateShow(UUID showId, AdminUpdateShowRequest request, UUID userId, String role);
    AdminShowDetailResponse getShowDetail(UUID showId, UUID userId, String role);
    List<AdminShowResponse> getAllShows(UUID userId, String role);
    void deleteShow(UUID showId, UUID userId,String role);
    /*
     * session
     * */
    Optional<AdminCreateSessionResponse> createSession(UUID showId, AdminCreateSessionRequest request,UUID userId);
    Optional<AdminUpdateSessionResponse> updateSession(UUID sessionId, AdminUpdateSessionRequest request, UUID userId, String role);
    AdminSessionDetailResponse getSessionDetail(UUID sessionId, UUID userId, String role);
    List<AdminSessionResponse> getAllSessions(UUID userId, String role);
    void deleteSession(UUID sessionId, UUID userId, String role);

}