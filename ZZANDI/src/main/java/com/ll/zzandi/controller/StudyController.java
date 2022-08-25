package com.ll.zzandi.controller;

import com.ll.zzandi.domain.*;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.exception.StudyForm;
import com.ll.zzandi.service.BoardService;
import com.ll.zzandi.service.BookService;
import com.ll.zzandi.service.LectureService;
import com.ll.zzandi.service.StudyService;

import java.security.Principal;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final BookService bookService;
    private final LectureService lectureService;
    private final BoardService boardService;

    @GetMapping("/study/create")
    public String createStudy(StudyForm studyForm) {
        return "study/studyForm";
    }

    @PostMapping("/study/create")
    public String createStudy(@Valid StudyForm studyForm, BindingResult bindingResult, BookDto bookDto, LectureDto lectureDto) {
        if (bindingResult.hasErrors()) {
            return "study/studyForm";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보
        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            Book book = bookService.save(bookDto);
            studyService.createStudyWithBook(studyForm, book , user);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            Lecture lecture = lectureService.save(lectureDto);
            studyService.createStudyWithLecture(studyForm, lecture , user);
        }
        return "redirect:/";
    }

    @GetMapping("/study/list")
    public String studyList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Study> studyList = studyService.findAll();
        model.addAttribute("studyList", studyList);
        model.addAttribute("user", user);
        return "study/studyList";
    }

    @GetMapping("/study/detail/{studyId}")
    public String detailStudy(Model model, @PathVariable Long studyId) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(null);
        Book books = studies.getBook();
        Lecture lectures = studies.getLecture();
        if (books != null) {
            books = bookService.findByid(books.getId()).orElseThrow(null);
        } else if (lectures != null) {
            lectures = lectureService.findById(lectures.getId()).orElseThrow(null);
        }

        model.addAttribute("studies", studies);
        model.addAttribute("books", books);
        model.addAttribute("lectures", lectures);
        return "study/studyDetail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/study/delete/{studyId}")
    public String deleteStudy(@PathVariable Long studyId, Principal principal) {
        Study studies = studyService.findByStudyId(studyId).orElseThrow(null);

        if(!studies.getUser().getUserId().equals(principal.getName().split(",")[1].substring(8,principal.getName().split(",")[1].length()))){
            return "study/studyError";
        }
        studyService.deleteStudy(studies);
        return "redirect:/";
    }

    @GetMapping("/study/modify/{studyId}")
    public String updateStudyForm(StudyForm studyForm) {
        return "study/studyModify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/study/modify/{studyId}")
    public String updateStudy(@Valid StudyForm studyForm, BindingResult bindingResult, @PathVariable Long studyId, BookDto bookDto, LectureDto lectureDto, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "study/studyModify";
        }
        Study studies = studyService.findByStudyId(studyId).orElseThrow(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보
        System.out.println("principal.getName() = " + principal.getName());
        if(!studies.getUser().getUserId().equals(principal.getName().split(",")[1].substring(8,principal.getName().split(",")[1].length()))){
            return "study/studyError";
        }
        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            studyService.updateStudyWithBook(studyId, studyForm, bookDto , user);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            studyService.updateStudyWithLecture(studyId, studyForm, lectureDto, user);
        }
        return "redirect:/";
    }
}
