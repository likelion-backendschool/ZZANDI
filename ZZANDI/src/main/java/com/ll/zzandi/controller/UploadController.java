package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final StudyService studyService;

    @GetMapping("/study/coverUpload/{studyId}")
    public String StudyCover(@PathVariable long studyId, Model model){
        model.addAttribute("studyId", studyId);
        return "/study/studyCoverUpload";
    }

    @PostMapping("/study/coverUpload/{studyId}")
    @ResponseBody
    @Transactional
    public String getStudyCover(@RequestParam("coverImage")MultipartFile multipartFile  , @PathVariable long studyId , @AuthenticationPrincipal Study study) throws IOException {
        studyService.updateCoverImg(multipartFile, studyId);
        return "1";
    }
}