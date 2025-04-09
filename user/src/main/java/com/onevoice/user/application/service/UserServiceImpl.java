package com.onevoice.user.application.service;

import com.onevoice.common.security.UserRole;
import com.onevoice.user.application.dto.FindUserQuery;
import com.onevoice.user.application.dto.LoginRequestDto;
import com.onevoice.user.application.dto.SignupRequestDto;
import com.onevoice.user.domain.User;
import com.onevoice.user.domain.repository.UserRepository;
import com.onevoice.user.exception.DuplicateUserException;
import com.onevoice.user.exception.PasswordNotMatchException;
import com.onevoice.user.exception.UserNotFoundException;
import com.onevoice.user.presentation.dto.request.DeleteUserRequestDto;
import com.onevoice.user.presentation.dto.response.DeleteUserResponseDto;
import com.onevoice.user.presentation.dto.response.GetUserInfoResponseDto;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User user = User.createUser(command.email(), command.password(), UserRole.USER,
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

    @Override
    public Optional<FindUserQuery> findUserById(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return Optional.of(FindUserQuery.of(user));
    }

    @Override
    public GetUserInfoResponseDto getMyInfo(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return GetUserInfoResponseDto.of(user);
    }

    @Override
    @Transactional
    public DeleteUserResponseDto deleteUser(UUID userId, DeleteUserRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().matches(requestDto.password(), passwordEncoder)) {
            throw new PasswordNotMatchException();
        }

        user.delete(userId);

        return DeleteUserResponseDto.of(user);
    }
}
