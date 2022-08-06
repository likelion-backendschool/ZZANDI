package com.ll.zzandi.controller;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public User join(@RequestBody UserRequestDto userRequestDto){
        User user = userService.join(userRequestDto);
        System.out.println(user);
        return user;
    }
}