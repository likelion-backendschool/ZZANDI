package com.ll.zzandi.controller;

import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/join")
    public String showSignForm(UserDto.RegisterRequest registerRequest) {
        return "Sign-up";
    }
    @PostMapping("/join")
    public String join(Model model, @Valid UserDto.RegisterRequest registerRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "Sign-up";
        }
        var response = userService.join(registerRequest).toResponse();
        model.addAttribute("user",response);
        return "Result";
    }
}