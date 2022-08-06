package com.ll.zzandi.service;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.exception.ErrorCode;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(final UserRequestDto userRequestDto) {
        userRepository.findByUserId(userRequestDto.getUserId()).ifPresent(x -> {
            throw new UserApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userRequestDto.getUserId()));
        });
        userRequestDto.encodePassword(passwordEncoder);
        return userRepository.save(User.of(userRequestDto));
    }
}


