package com.ll.zzandi.controller;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showSignForm(UserRequestDto userRequestDto) {
        return "Sign-up";
    }
    @PostMapping("/join")
    public String join(Model model, @Valid UserRequestDto userRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "Sign-up";
        }
        User user = userService.join(userRequestDto);
        model.addAttribute("userid",user.getUserId());
        return "Result";
    }
}