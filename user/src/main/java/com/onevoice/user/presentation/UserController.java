package com.onevoice.user.presentation;

import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.OAuth2SignupRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.application.service.UserService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Optional<FindUserQuery> signup(@RequestBody SignupRequestDto command) {
        return userService.signup(command);
    }

    @PostMapping("/login")
    public Optional<FindUserQuery> login(@RequestBody LoginRequestDto command) {
        return userService.login(command);
    }

    @GetMapping("/{userId}")
    public Optional<FindUserQuery> findUserById(@PathVariable UUID userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("/test")
    public Optional<FindUserQuery> testMethod(@AuthenticationPrincipal UUID userId) {

        return userService.findUserById(userId);
    }

    /*
    Use OAuth2
     */
    @GetMapping("/oauth2/{email}")
    public Optional<FindUserQuery> findUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @PostMapping("/oauth2")
    public Optional<FindUserQuery> signupByOAuth2(@RequestBody OAuth2SignupRequestDto command) {
        return userService.signupByOAuth2(command);
    }

}
