package com.onevoice.user.application.service;

import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.domain.User;
import com.onevoice.user.domain.UserRole;
import com.onevoice.user.domain.repository.UserRepository;
import com.onevoice.user.exception.DuplicateUserException;
import com.onevoice.user.exception.PasswordNotMatchException;
import com.onevoice.user.exception.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<FindUserQuery> signup(SignupRequestDto command) {

        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new DuplicateUserException();
        }

        User user = User.createUser(command.email(), command.password(), UserRole.CUSTOMER,
            passwordEncoder);

        User saved = userRepository.save(user);

        return Optional.of(FindUserQuery.of(saved));
    }

    @Override
    public Optional<FindUserQuery> login(LoginRequestDto command) {

        User user = userRepository.findByEmail(command.email())
            .orElseThrow(UserNotFoundException::new);

        if(!user.getPassword().matches(command.password(), passwordEncoder)){
            throw new PasswordNotMatchException();
        }

        return Optional.of(FindUserQuery.of(user));
    }
}
