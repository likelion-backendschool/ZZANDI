package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.ToDoType;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.service.StudyService;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityExistsException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    private final UserRepository userRepository;
    private final StudyService studyService;
    private final ToDoListService toDoListService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user, Model model, @RequestParam(defaultValue = "ALL") String st, @RequestParam(defaultValue = "ALL") String ss, @RequestParam(defaultValue = "") String kw){
        if(user!=null) {
            List<ToDoList> toDoLists = toDoListService.findAllByUserAndType(user, ToDoType.DOING);
            model.addAttribute("toDoList", toDoLists);
        }
        List<Study> studyList = studyService.getList(st, ss, kw);
        model.addAttribute("studyList", studyList);
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
