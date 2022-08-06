package com.ll.zzandi.service;

import com.ll.zzandi.domain.Users;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Users join(UserRequestDto userRequestDto) {
        Users users = new Users(userRequestDto.getUserId(),userRequestDto.getUserPassword(),userRequestDto.getUserEmail(), userRequestDto.getUserNickname());
        return userRepository.save(users);
    }
}


