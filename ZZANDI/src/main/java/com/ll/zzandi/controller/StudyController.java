package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Book;
import com.ll.zzandi.domain.Lecture;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.exception.StudyForm;
import com.ll.zzandi.service.BookService;
import com.ll.zzandi.service.LectureService;
import com.ll.zzandi.service.StudyService;

import java.security.Principal;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final BookService bookService;
    private final LectureService lectureService;

    @GetMapping("/study/create")
    public String studyCreate(StudyForm studyForm) {
        return "study/studyForm";
    }

    @PostMapping("/study/create")
    public String studyCreate(@Valid StudyForm studyForm, BindingResult bindingResult, BookDto bookDto, LectureDto lectureDto) {
        if (bindingResult.hasErrors()) {
            return "study/studyForm";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보
        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            Book book = bookService.save(bookDto);
            studyService.saveWithBook(studyForm, book , user);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            Lecture lecture = lectureService.save(lectureDto);
            studyService.saveWithLecture(studyForm, lecture , user);
        }
        return "redirect:/";
    }

    @GetMapping("/study/list")
    public String list(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Study> studyList = studyService.findall();
        model.addAttribute("studyList", studyList);
        model.addAttribute("user", user);
        return "study/studyList";
    }

    @GetMapping("/study/detail/{studyId}")
    public String detail(Model model, @PathVariable Long studyId) {
        Study studies = studyService.findByid(studyId).orElseThrow(null);
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
    public String delete(@PathVariable Long studyId, Principal principal) {
        Study studies = studyService.findByid(studyId).orElseThrow(null);
        if(!studies.getUser().getUserId().equals(principal.getName().split(",")[1].substring(8,principal.getName().split(",")[1].length()))){
            return "study/studyError";
        }
        studyService.delete(studies);
        return "redirect:/";
    }

    @GetMapping("/study/modify/{studyId}")
    public String modify(StudyForm studyForm) {
        return "study/studyModify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/study/modify/{studyId}")
    public String modify(@Valid StudyForm studyForm, BindingResult bindingResult, @PathVariable Long studyId, BookDto bookDto, LectureDto lectureDto, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "study/studyModify";
        }
        Study studies = studyService.findByid(studyId).orElseThrow(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보
        System.out.println("principal.getName() = " + principal.getName());
        if(!studies.getUser().getUserId().equals(principal.getName().split(",")[1].substring(8,principal.getName().split(",")[1].length()))){
            return "study/studyError";
        }
        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            studyService.modifyWithBook(studyId, studyForm, bookDto , user);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            studyService.modifyWithLecture(studyId, studyForm, lectureDto, user);
        }
        return "redirect:/";
    }
}
