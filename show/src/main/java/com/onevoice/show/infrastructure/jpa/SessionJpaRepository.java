package com.onevoice.show.infrastructure.jpa;

import com.onevoice.show.domain.Session;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionJpaRepository extends JpaRepository<Session, UUID> {

    @Query("SELECT s FROM Session s JOIN FETCH s.show")
    List<Session> findAllWithShow();
}
