package com.ll.zzandi.controller;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(() -> new UserApplicationException(ErrorType.NOT_FOUND));
            model.addAttribute("user", currentUser);
        }
        return "index";
    }

    //TODO 예외처리 확인하기 위한 단순한 테스트 api 추후 삭제 예정
    @GetMapping("/test")
    public String test(){
        //강제로 에러 만들기
        User user=userRepository.findById(999L).orElseThrow(RuntimeException::new);
        return "index";
    }
}
