package com.ll.zzandi.service;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User join(final UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getUserId(),userRequestDto.getUserPassword(),userRequestDto.getUserEmail(), userRequestDto.getUserNickname());
        return userRepository.save(user);
    }
}


