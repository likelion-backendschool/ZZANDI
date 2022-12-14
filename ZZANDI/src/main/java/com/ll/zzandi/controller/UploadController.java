package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.StudyException;
import com.ll.zzandi.service.StudyService;
import com.ll.zzandi.util.aws.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ImageUploadService imageUploadService;

    @PostMapping("/change/url")
    @ResponseBody
    public String findImageUrl(@RequestParam("image") MultipartFile imageFile) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        String fileExt = originalFilename.substring(originalFilename.indexOf("."));
        String saveFileName = RandomStringUtils.randomAlphanumeric(30).concat(fileExt);

        return imageUploadService.upload(saveFileName, imageFile);
    }

    @GetMapping("/study/coverUpload/{studyId}")
    public String StudyCover(@AuthenticationPrincipal User user, @PathVariable long studyId, Model model){
        Study study = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(
            ErrorType.NOT_FOUND));
        if (!study.getUser().getId().equals(user.getId())|| study.getStudyStatus() == StudyStatus.COMPLETE) {
            throw new StudyException(ErrorType.NOT_LEADER);
        }
        model.addAttribute("study", study);
        model.addAttribute("studyId", study.getId());
        return "study/studyCoverUpload";
    }

    @PostMapping("/study/coverUpload/{studyId}")
    @ResponseBody
    @Transactional
    public String getStudyCover(@AuthenticationPrincipal User user, @RequestParam("croppedImage") MultipartFile multipartFile  , @PathVariable long studyId) throws IOException {
        Study study = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(
            ErrorType.NOT_FOUND));
        if (!study.getUser().getId().equals(user.getId())|| study.getStudyStatus() == StudyStatus.COMPLETE) {
            throw new StudyException(ErrorType.NOT_LEADER);
        }
        studyService.updateCoverImg(multipartFile, studyId);
        return "1";
    }

    @PostMapping("/study/delete")
    @ResponseBody
    public String deleteFile(@RequestParam("ImageUrl") String fileUrl){
        imageUploadService.deleteFile(fileUrl);
    return "success";
    }
}