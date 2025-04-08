package com.onevoice.show.domain.repository;

import com.onevoice.show.domain.Show;
import java.util.Optional;

public interface ShowRepository {

    Optional<Show> findByTitle(String title);

    Show save(Show show);
}
