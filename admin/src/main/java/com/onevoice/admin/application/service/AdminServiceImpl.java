package com.onevoice.admin.application.service;

import com.onevoice.admin.application.client.ShowClient;
import com.onevoice.admin.presentation.dto.client.session.*;
import com.onevoice.admin.presentation.dto.client.show.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ShowClient showClient;

    /*
    * show
    * */
    @Override
    public Optional<AdminCreateShowResponse> createShow(AdminCreateShowRequest request) {
        return showClient.createShow(request);
    }

    @Override
    public Optional<AdminUpdateShowResponse> updateShow(UUID showId, AdminUpdateShowRequest request, UUID userId, String role) {
        return showClient.updateShow(showId, request, userId, role);
    }


    @Override
    public AdminShowDetailResponse getShowDetail(UUID showId, UUID userId, String role) {
        return showClient.getShowDetail(showId, userId, role);
    }

    @Override
    public List<AdminShowResponse> getAllShows(UUID userId, String role) {
        return showClient.getAllShows(userId, role);
    }
    @Override
    public void deleteShow(UUID showId, UUID userId,String role) {
        showClient.deleteShow(showId, userId, role);
    }

    /*
    * session
    * */

    @Override
    public Optional<AdminCreateSessionResponse> createSession(UUID showId, AdminCreateSessionRequest request, UUID userId) {
        return showClient.createSession(showId, request, userId);
    }

    @Override
    public Optional<AdminUpdateSessionResponse> updateSession(UUID sessionId, AdminUpdateSessionRequest request, UUID userId, String role) {
        return showClient.updateSession(sessionId, request, userId, role);
    }

    @Override
    public AdminSessionDetailResponse getSessionDetail(UUID sessionId, UUID userId, String role) {
        return showClient.getSessionDetail(sessionId, userId, role);
    }

    @Override
    public List<AdminSessionResponse> getAllSessions(UUID userId, String role) {
        return showClient.getAllSessions(userId, role);
    }
    @Override
    public void deleteSession(UUID sessionId, UUID userId,String role) {
        showClient.deleteSession(sessionId, userId, role);
    }
}
