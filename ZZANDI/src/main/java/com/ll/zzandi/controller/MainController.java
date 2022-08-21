package com.ll.zzandi.controller;

import com.ll.zzandi.config.security.UserContext;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityExistsException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    private final UserRepository userRepository;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user, Model model){
        if(user!=null) {
            User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(EntityExistsException::new);
            model.addAttribute("user", currentUser);
        }
        return "index";
    }
}
