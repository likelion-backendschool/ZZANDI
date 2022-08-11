package com.ll.zzandi.controller;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.repository.UserRepository;
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
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/join")
    public String showSignForm(UserDto.RegisterRequest registerRequest) {
        return "Sign-up";
    }

    @PostMapping("/join")
    public String join(Model model, @Valid UserDto.RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Sign-up";
        }
        var response = userService.join(registerRequest).toResponse();
        model.addAttribute("user", response);
        return "Result";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        User user = userRepository.findByUserEmail(email);
        String view = "user/checked-email";
        if (user == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if (!user.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        userService.completeSignUp(user);
        model.addAttribute("numberOfUser", userRepository.count());
        model.addAttribute("nickname", user.getUserNickname());
        return view;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/user/login";
    }
}