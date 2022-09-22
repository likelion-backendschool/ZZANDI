package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.study.StudyListDto;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    private final UserRepository userRepository;
    private final StudyService studyService;
    private final ToDoListService toDoListService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user, Model model, @RequestParam(defaultValue = "ALL") String tag){
        if(user != null) {
            List<ToDoList> toDoLists = toDoListService.findAllByUserAndType(user, ToDoType.DOING);
            model.addAttribute("toDoList", toDoLists);
            List<StudyListDto> myStudyList = studyService.findMyStudyList(user);
            model.addAttribute("myStudyList", myStudyList);
        }

        List<StudyListDto> newStudyList = studyService.findNewStudyList();
        model.addAttribute("newStudyList", newStudyList);

        List<StudyListDto> fieldStudyList = studyService.findFieldStudyList(tag);
        model.addAttribute("fieldStudyList", fieldStudyList);
        return "index";
    }

    @GetMapping("/fieldStudyList")
    @ResponseBody
    public List<StudyListDto> findFieldStudy(@RequestParam(defaultValue = "ALL") String tag) {
        return studyService.findFieldStudyList(tag);
    }

    //TODO 예외처리 확인하기 위한 단순한 테스트 api 추후 삭제 예정
    @GetMapping("/test")
    public String test(){
        //강제로 에러 만들기
        User user=userRepository.findById(999L).orElseThrow(RuntimeException::new);
        return "index";
    }
}
