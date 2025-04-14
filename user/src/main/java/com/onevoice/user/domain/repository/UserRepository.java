package com.onevoice.user.domain.repository;

import com.onevoice.user.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);
    User save(User user);

}
