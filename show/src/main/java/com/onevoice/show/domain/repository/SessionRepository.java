package com.onevoice.show.domain.repository;

import com.onevoice.show.domain.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Session save(Session session);

    List<Session> findAll();

    List<Session> findByShowId(UUID showId);

    Optional<Session> findById(UUID sessionId);
}
