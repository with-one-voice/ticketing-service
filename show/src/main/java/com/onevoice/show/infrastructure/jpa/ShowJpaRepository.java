package com.onevoice.show.infrastructure.jpa;

import com.onevoice.show.domain.Show;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowJpaRepository extends JpaRepository<Show, UUID> {

}
