package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Book;
import com.ll.zzandi.domain.Lecture;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.exception.StudyForm;
import com.ll.zzandi.service.BookService;
import com.ll.zzandi.service.LectureService;
import com.ll.zzandi.service.StudyService;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/study/create")
    public String studyCreate(StudyForm studyForm) {
        return "study/studyForm";
    }

    @PostMapping("/study/create")
    public String studyCreate(@Valid StudyForm studyForm, BindingResult bindingResult, BookDto bookDto, LectureDto lectureDto) {
        if (bindingResult.hasErrors()) {
            return "study/studyForm";
        }

        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            Book book = bookService.save(bookDto);
            studyService.saveWithBook(studyForm, book);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            Lecture lecture = lectureService.save(lectureDto);
            studyService.saveWithLecture(studyForm, lecture);
        }
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

    @GetMapping("/study/delete/{studyId}")
    public String delete(@PathVariable Long studyId) {
        Study studies = studyService.findByid(studyId).orElseThrow(null);
        studyService.delete(studies);
        return "redirect:/";
    }

    @GetMapping("/study/modify/{studyId}")
    public String modify(StudyForm studyForm) {
        return "study/studyModify";
    }

    @PostMapping("/study/modify/{studyId}")
    public String modify(@Valid StudyForm studyForm, BindingResult bindingResult, @PathVariable Long studyId, BookDto bookDto, LectureDto lectureDto) {
        if (bindingResult.hasErrors()) {
            return "study/studyModify";
        }

        if (Stream.of(bookDto.getBookName(), bookDto.getBookPage(), bookDto.getBookAuthor(),
            bookDto.getBookPublisher(), bookDto.getBookUrl()).allMatch(Objects::nonNull)) {
            studyService.modifyWithBook(studyId, studyForm, bookDto);
        } else if (Stream.of(lectureDto.getLecturer(), lectureDto.getLectureName(),
            lectureDto.getLecturer(), lectureDto.getLectureNumber()).allMatch(Objects::nonNull)) {
            studyService.modifyWithLecture(studyId, studyForm, lectureDto);
        }
        return "redirect:/";
    }
}
