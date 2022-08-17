package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/study/create")
    public String studyCreate() {
        return "study/studyForm";
    }

    @PostMapping("/study/create")
    public String studyCreate(StudyDto studyDto) {
        studyService.save(studyDto);
        return "redirect:/";
    }

    @GetMapping("/study/list")
    public String list(Model model) {
        List<Study> studyList = studyService.findall();
        model.addAttribute("studyList", studyList);
        return "study/studyList";
    }

    @GetMapping("/study/detail/{studyId}")
    public String detail(Model model, @PathVariable Long studyId) {
        Study studies = studyService.findByid(studyId).get();
        model.addAttribute("studies", studies);
        return "study/studyDetail";
    }

    @GetMapping("/study/delete/{studyId}")
    public String delete(@PathVariable Long studyId) {
        Study studies = studyService.findByid(studyId).get();
        studyService.delete(studies);
        return "redirect:/";
    }

    @GetMapping("/study/modify/{studyId}")
    public String modify(Model model, @PathVariable Long studyId){
        return "study/studyModify";
    }

    @PostMapping("/study/modify/{studyId}")
    public String modify(StudyDto studyDto, @PathVariable Long studyId) {
        studyService.modify(studyId, studyDto);
        return "redirect:/";
    }

}
