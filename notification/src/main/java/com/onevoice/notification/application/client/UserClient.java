package com.onevoice.notification.application.client;

import com.onevoice.notification.application.dto.query.FindUserQuery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/{userId}")
    Optional<FindUserQuery> findUserById(@PathVariable UUID userId);
}