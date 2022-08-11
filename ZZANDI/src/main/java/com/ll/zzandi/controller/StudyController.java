package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class StudyController {

    private final StudyService studyService;
    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }


    @GetMapping("/study/new")
    public String createForm() {
        return "study/createStudyForm";
    }

    @PostMapping("/study/new")
    public String create(StudyForm form){
        Study study = new Study();
        study.setStudyTitle(form.getTitle());
        study.setStudyPeople(form.getPeople());
        study.setStudyStart(form.getStartDate());
        study.setStudyEnd(form.getEndDate());

        studyService.join(study);

        return "redirect:/";
    }

    @GetMapping("/study/list")
    public String list(Model model) {
        List<Study> studies = studyService.findStudies();
        model.addAttribute("studies", studies);
        return "/study/studyList";
    }

    @GetMapping("/study/detail/{studyId}")
    public String detail(Model model, @PathVariable Long studyId) {

        Study studies =  studyService.findOne(studyId).get();
        model.addAttribute("studies", studies);

        return "/study/studyDetail";
    }

    @GetMapping("/study/delete/{studyId}")
    public String delete(@PathVariable Long studyId){

        Study studies =  studyService.findOne(studyId).get();
        studyService.delete(studies, studyId);

        return "redirect:/";


    }


}