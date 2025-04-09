package com.onevoice.show.domain.repository;

import com.onevoice.show.domain.Show;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ShowRepository {

    Optional<Show> findByTitle(String title);

    Show save(Show show);

    Optional<Show> findById(UUID showId);

    List<Show> findAll();

    List<Show> search(String keyword, Pageable pageable);

    Long getTotal(String keyword);
}
