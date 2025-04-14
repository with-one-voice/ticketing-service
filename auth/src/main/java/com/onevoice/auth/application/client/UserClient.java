package com.onevoice.auth.application.client;

import com.onevoice.auth.application.dto.FindUserQuery;
import com.onevoice.auth.presentation.dto.request.LoginRequestDto;
import com.onevoice.auth.presentation.dto.request.SignupRequestDto;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/signup")
    Optional<FindUserQuery> signup(@RequestBody SignupRequestDto command);

    @PostMapping("/login")
    Optional<FindUserQuery> login(@RequestBody LoginRequestDto command);
}
