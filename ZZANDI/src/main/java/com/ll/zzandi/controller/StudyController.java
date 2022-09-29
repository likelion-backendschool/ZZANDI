package com.ll.zzandi.controller;

import com.ll.zzandi.domain.*;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.BookInfoDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;

import com.ll.zzandi.dto.api.SearchDto;
import com.ll.zzandi.dto.study.StudyDetailDto;
import com.ll.zzandi.dto.study.StudyListDto;
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
import org.springframework.data.domain.Page;
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
        return "redirect:detail/%d".formatted(study.getId());
    }

    @GetMapping("/study/list")
    public String findStudyList(@RequestParam(defaultValue = "ALL") String st, @RequestParam(defaultValue = "ALL") String ss, @RequestParam(defaultValue = "ALL") String tag, @RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("st", st);
        model.addAttribute("ss", ss);
        model.addAttribute("kw", kw);
        model.addAttribute("tag", tag);
        model.addAttribute("page", page);
        return "study/studyList";
    }

    @GetMapping("/study/list-data")
    @ResponseBody
    public Page<StudyListDto> findStudyListPaging(@RequestParam(defaultValue = "ALL") String st, @RequestParam(defaultValue = "ALL") String ss, @RequestParam(defaultValue = "ALL") String tag, @RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "0") int page) {
        return studyService.findStudyListPaging(st, ss, tag, kw, page);
    }

    @GetMapping("/study/detail/{studyId}")
    public String StudyDetail(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model) {
        studyService.updateViews(studyId);
        model.addAttribute("studyId", studyId);
        return "study/studyDetailAsync";
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
        return "study/studyDetailAsync";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/study/delete/{studyId}")
    public String deleteStudy(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
        if (!studies.getUser().getId().equals(user.getId()) || studies.getStudyStatus() == StudyStatus.COMPLETE) {
            throw new StudyException(ErrorType.NOT_LEADER);
        }

        studyService.deleteStudy(studies);
        return "redirect:";
    }

    @GetMapping("/study/update/{studyId}")
    public String updateStudyForm(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model, StudyDto studyDto) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
        if (!studies.getUser().getId().equals(user.getId())|| studies.getStudyStatus() == StudyStatus.PROGRESS || studies.getStudyStatus() == StudyStatus.COMPLETE) {
            throw new StudyException(ErrorType.NOT_LEADER);
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
            throw new StudyException(ErrorType.NOT_LEADER);
        }

        if (bindingResult.hasErrors()) {
            return "study/studyUpdate";
        }
        if (studyDto.getStudyType().equals("BOOK")) {
            studyService.updateStudyWithBook(studyId, studyDto, bookDto, user);
        } else if (studyDto.getStudyType().equals("LECTURE")) {
            studyService.updateStudyWithLecture(studyId, studyDto, lectureDto, user);
        }

        return "redirect:detail/%d".formatted(studyId);
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
    @RequestMapping("study/studyBookSearch")
    public String search() {
        return "study/studyBookSearch";
    }
}
