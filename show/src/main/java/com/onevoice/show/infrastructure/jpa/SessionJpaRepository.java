package com.onevoice.show.infrastructure.jpa;

import com.onevoice.show.domain.Session;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionJpaRepository extends JpaRepository<Session, UUID> {

}
