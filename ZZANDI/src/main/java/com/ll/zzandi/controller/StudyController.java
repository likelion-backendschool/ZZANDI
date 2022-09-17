package com.ll.zzandi.controller;

import com.ll.zzandi.domain.*;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.BookInfoDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;

import com.ll.zzandi.dto.api.SearchDto;
import com.ll.zzandi.dto.study.StudyDetailDto;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.StudyException;
import com.ll.zzandi.service.BookService;
import com.ll.zzandi.service.LectureService;
import com.ll.zzandi.service.StudyService;

import com.ll.zzandi.service.TeamMateService;
import com.ll.zzandi.service.UserService;

import java.net.URI;
import java.nio.charset.StandardCharsets;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyController {
    @Value("${aladin.key}")
    private  String TTB_KEY;

    @Value("${aladin.searchUrl}")
    private  String SEARCH_URL;

    @Value("${aladin.detailUrl}")
    private  String DETAIL_URL;


    private final StudyService studyService;
    private final BookService bookService;
    private final LectureService lectureService;
    private final TeamMateService teamMateService;
    private final UserService userService;

    @GetMapping("/study/create")
    public String createStudy(@AuthenticationPrincipal User user, StudyDto studyDto, Model model) {
        return "study/studyForm";
    }

    @PostMapping("/study/create")
    public String createStudy(@AuthenticationPrincipal User user, @Valid StudyDto studyDto, BindingResult bindingResult, BookDto bookDto, LectureDto lectureDto) {
        if (bindingResult.hasErrors()) {
            return "study/studyForm";
        }
        Study study = null;
        if (studyDto.getStudyType().equals("BOOK")) {
            RestTemplate restTemplate = new RestTemplate();
            URI targetUrl = UriComponentsBuilder
                    .fromHttpUrl(DETAIL_URL)
                    .queryParam("ItemId", bookDto.getBookIsbn())
                    .queryParam("ttbkey", TTB_KEY)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            BookInfoDto bookInfoDto = restTemplate.getForEntity(targetUrl, BookInfoDto.class).getBody();
            Book book = bookService.save(bookInfoDto);
            study = studyService.createStudyWithBook(studyDto, book, user);
        } else if (studyDto.getStudyType().equals("LECTURE")) {
            Lecture lecture = lectureService.save(lectureDto);
            study = studyService.createStudyWithLecture(studyDto, lecture, user);
        }
        teamMateService.createTeamMate(user, study.getId());
        return "redirect:/study/detail/%d".formatted(study.getId());
    }

    @GetMapping("/study/list")
    public String studyList(@AuthenticationPrincipal User user, Model model,@RequestParam(defaultValue = "ALL") String st,@RequestParam(defaultValue = "ALL") String ss, @RequestParam(defaultValue = "") String kw) {
        List<Study> studyList = studyService.getList(st, ss, kw);
        model.addAttribute("studyList", studyList);
        return "study/studyList";
    }

    @GetMapping("/study/detail/{studyId}")
    public String detailStudy(@AuthenticationPrincipal User user, Model model, @PathVariable Long studyId) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(null);
        Book books = studies.getBook();
        Lecture lectures = studies.getLecture();

        // 상세검색 기능 (시작)
        if (books != null) {
            books = bookService.findByid(books.getId()).orElseThrow(null);
        } else if (lectures != null) {
            lectures = lectureService.findById(lectures.getId()).orElseThrow(null);
        }
        // 상세검색 기능 (종료)

        // 권장 진도율 계산 (시작)
        int studyDays = studyService.getStudyDays(studyId);
        model.addAttribute("studyDays", studyDays);
        int studyPeriod = studyService.getStudyPeriod(studyId);
        model.addAttribute("studyPeriod", studyPeriod);
        int studyRecommend = studyService.getStudyRecommend(studyId);
        model.addAttribute("studyRecommend", studyRecommend);
        // 권장 진도율 계산 (종료)

        List<Boolean> checkList = teamMateService.checkTeamMate(studies.getTeamMateList(), user);
        model.addAttribute("studies", studies);
        model.addAttribute("books", books);
        model.addAttribute("lectures", lectures);
        model.addAttribute("user", user);
        model.addAttribute("isParticipation", checkList.get(0));
        model.addAttribute("isTeamMate", checkList.get(1));
        model.addAttribute("isDelete", checkList.get(2));
        return"study/studyDetail";
    }

    @GetMapping("/study/detail/{studyId}/study-data")
    @ResponseBody
    public StudyDetailDto findStudyDetail(@PathVariable Long studyId) {
        return studyService.findStudyDetail(studyId);
    }

    @GetMapping("/study/detail/{studyId}/test")
    public String testStudyDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model) {
        studyService.updateViews(studyId);
        model.addAttribute("studyId", studyId);

        int studyDays = studyService.getStudyDays(studyId);
        model.addAttribute("studyDays", studyDays);
        int studyPeriod = studyService.getStudyPeriod(studyId);
        model.addAttribute("studyPeriod", studyPeriod);
        int studyRecommend = studyService.getStudyRecommend(studyId);
        model.addAttribute("studyRecommend", studyRecommend);
        return "study/studyDetailAsync";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/study/delete/{studyId}")
    public String deleteStudy(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
        if (!studies.getUser().getId().equals(user.getId()) || studies.getStudyStatus() == StudyStatus.PROGRESS || studies.getStudyStatus() == StudyStatus.COMPLETE) {
            return "study/studyError";
        }
        studyService.deleteStudy(studies);
        return "redirect:/";
    }

    @GetMapping("/study/update/{studyId}")
    public String updateStudyForm(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model, StudyDto studyDto) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
        if (!studies.getUser().getId().equals(user.getId())|| studies.getStudyStatus() == StudyStatus.PROGRESS || studies.getStudyStatus() == StudyStatus.COMPLETE) {
            return "study/studyError";
        }

        Book books = studies.getBook();
        Lecture lectures = studies.getLecture();
        StudyDto newStudyDto = studyService.saveNewStudyDto(studyId, studyDto);
        model.addAttribute("studies", studies);
        model.addAttribute("lectures", lectures);
        model.addAttribute("books", books);
        model.addAttribute("studyDto", newStudyDto);
        return "study/studyUpdate";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/study/update/{studyId}")
    public String updateStudy(@AuthenticationPrincipal User user, @Valid StudyDto studyDto, BindingResult bindingResult, @PathVariable Long studyId, BookDto bookDto, LectureDto lectureDto, Model model) {

        Study studies = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
        if (!studies.getUser().getId().equals(user.getId()) || studies.getStudyStatus() == StudyStatus.PROGRESS || studies.getStudyStatus() == StudyStatus.COMPLETE) {
            return "study/studyError";
        }

        if (bindingResult.hasErrors()) {
            return "study/studyUpdate";
        }
        if (studyDto.getStudyType().equals("BOOK")) {
            studyService.updateStudyWithBook(studyId, studyDto, bookDto, user);
        } else if (studyDto.getStudyType().equals("LECTURE")) {
            studyService.updateStudyWithLecture(studyId, studyDto, lectureDto, user);
        }

        return "redirect:/study/detail/%d".formatted(studyId);
    }

    @GetMapping("/study/search/book")
    @ResponseBody
    public SearchDto searchBook(@RequestParam("query")String bookKeyword){
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder
                .fromHttpUrl(SEARCH_URL)
                .queryParam("Query", bookKeyword)
                .queryParam("ttbkey", TTB_KEY)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        try {
            SearchDto dtoResponseEntity = restTemplate.getForEntity(targetUrl, SearchDto.class).getBody();
            System.out.println(dtoResponseEntity.getItem().get(0).getTitle());
            return dtoResponseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
