package com.onevoice.show.infrastructure;

import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.infrastructure.jpa.ShowJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowRepositoryImpl implements ShowRepository {

    private final ShowJpaRepository showJpaRepository;

}
