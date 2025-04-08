package com.onevoice.user.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.application.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Secured("ROLE_USER")
    @GetMapping("/test")
    public ResponseEntity<CommonResponse<String>> testMethod(){
        return CommonResponse.success("호날두 사랑해");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/ronaldo")
    public ResponseEntity<CommonResponse<String>> testRonaldo(){
        return CommonResponse.success("유기체 역사상 GOAT");
    }
}
