package com.onevoice.user.application.service;

import com.onevoice.common.security.UserRole;
import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.OAuth2SignupRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.domain.User;
import com.onevoice.user.domain.repository.UserRepository;
import com.onevoice.user.exception.DuplicateUserException;
import com.onevoice.user.exception.PasswordNotMatchException;
import com.onevoice.user.exception.UserNotFoundException;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<FindUserQuery> signup(SignupRequestDto command) {

        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new DuplicateUserException();
        }

        User user = User.createUser(command.email(), command.password(), UserRole.USER,
            passwordEncoder);

        User saved = userRepository.save(user);

        return Optional.of(FindUserQuery.of(saved));
    }

    @Override
    public Optional<FindUserQuery> login(LoginRequestDto command) {

        User user = userRepository.findByEmail(command.email())
            .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().matches(command.password(), passwordEncoder)) {
            throw new PasswordNotMatchException();
        }

        return Optional.of(FindUserQuery.of(user));
    }

    @Override
    public Optional<FindUserQuery> findUserById(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return Optional.of(FindUserQuery.of(user));
    }

    /*
    User by OAuth2
     */
    @Override
    public Optional<FindUserQuery> findUserByEmail(String email) {

        // 찾는 유저가 없으면 사용자 등록을 진행해야 하므로 exception 을 던지지 않는다.
        return userRepository.findByEmail(email)
            .map(FindUserQuery::of);
    }

    @Override
    public Optional<FindUserQuery> signupByOAuth2(OAuth2SignupRequestDto command) {

        // email check 후 요청이므로
        User user = User.createUser(command.email(), command.name(), UserRole.USER,
            command.provider(), passwordEncoder);

        User saved = userRepository.save(user);

        return Optional.of(FindUserQuery.of(saved));
    }
}
