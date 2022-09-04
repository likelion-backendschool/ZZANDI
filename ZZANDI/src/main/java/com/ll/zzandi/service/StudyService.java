package com.ll.zzandi.service;

import com.ll.zzandi.domain.*;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.BookInfoDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.BookRepository;
import com.ll.zzandi.repository.CommentRepository;
import com.ll.zzandi.repository.StudyRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final BookService bookService;
    private final LectureService lectureService;
    private final StudyRepository studyRepository;
    private final BookRepository bookRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Study createStudyWithBook(@Valid StudyDto studyDto, Book book, User user) {
        Study study = new Study(user, studyDto.getStudyTitle(), book, null, StudyType.BOOK,
                studyDto.getStudyStart(),
                studyDto.getStudyEnd(), studyDto.getStudyPeople(), studyDto.getStudyTag(), 0,
                StudyStatus.RECRUIT);
        return studyRepository.save(study);
    }

    public Study createStudyWithLecture(@Valid StudyDto studyDto, Lecture lecture, User user) {
        Study study = new Study(user, studyDto.getStudyTitle(), null, lecture, StudyType.LECTURE,
                studyDto.getStudyStart(),
                studyDto.getStudyEnd(), studyDto.getStudyPeople(), studyDto.getStudyTag(), 0,
                StudyStatus.RECRUIT);
        return studyRepository.save(study);
    }

    public List<Study> findAll() {
        return studyRepository.findAll();
    }

    public Optional<Study> findByStudyId(Long studyId) {
        return studyRepository.findById(studyId);
    }

    @Transactional
    public void deleteStudy(Study studies) {
        List<Board> boardList = boardRepository.findBoardListByStudyId(studies.getId());
        for (Board board : boardList) {
            commentRepository.deleteCommentByBoardId(board.getId());
        }
        boardRepository.deleteBoardByStudyId(studies.getId());
        studyRepository.delete(studies);
    }

    public void updateStudyWithBook(Long studyId, @Valid StudyDto studyDto, BookInfoDto bookDto, User user) {
        Study s1 = studyRepository.findById(studyId).orElseThrow(null);
        Book book;
        if (s1.getBook() != null) {
            book = bookService.modify(s1.getBook().getId(), bookDto);
        } else {
            book = bookService.save(bookDto);
        }

        Lecture lecture = s1.getLecture();
        s1.setUser(user);
        s1.setId(studyId);
        s1.setStudyTitle(studyDto.getStudyTitle());
        s1.setBook(book);
        s1.setLecture(null);
        s1.setStudyType(StudyType.BOOK);
        s1.setStudyStart(studyDto.getStudyStart());
        s1.setStudyEnd(studyDto.getStudyEnd());
        s1.setStudyPeople(studyDto.getStudyPeople());
        s1.setStudyTag(studyDto.getStudyTag());
        studyRepository.save(s1);

        if (lecture != null) {
            lectureService.delete(lecture);
        }
    }

    public void updateStudyWithLecture(Long studyId, StudyDto studyDto, LectureDto lectureDto, User user) {
        Study s1 = studyRepository.findById(studyId).orElseThrow(null);
        Lecture lecture;
        if (s1.getLecture() != null) {
            lecture = lectureService.modify(s1.getLecture().getId(), lectureDto);
        } else {
            lecture = lectureService.save(lectureDto);
        }

        Book book = s1.getBook();
        s1.setUser(user);
        s1.setId(studyId);
        s1.setStudyTitle(studyDto.getStudyTitle());
        s1.setBook(null);
        s1.setLecture(lecture);
        s1.setStudyType(StudyType.LECTURE);
        s1.setStudyStart(studyDto.getStudyStart());
        s1.setStudyEnd(studyDto.getStudyEnd());
        s1.setStudyPeople(studyDto.getStudyPeople());
        s1.setStudyTag(studyDto.getStudyTag());
        studyRepository.save(s1);

        if (book != null) {
            bookRepository.delete(book);
        }
    }

    public StudyDto saveNewStudyDto(Long studyId, StudyDto studyDto) {
        Study studies = studyRepository.findById(studyId).orElseThrow(null);
        Book books = studies.getBook();
        Lecture lectures = studies.getLecture();

        studyDto.setStudyTitle(studies.getStudyTitle());
        studyDto.setStudyStart(studies.getStudyStart());
        studyDto.setStudyEnd(studies.getStudyEnd());
        studyDto.setStudyPeople(studies.getStudyPeople());
        studyDto.setStudyType(String.valueOf(studies.getStudyType()));
        studyDto.setStudyTag(studies.getStudyTag());
        if (studyDto.getStudyType().equals("BOOK")) {
            studyDto.setBookName(books.getBookName());
            studyDto.setBookPage(books.getBookPage());
            studyDto.setBookAuthor(books.getBookAuthor());
            studyDto.setBookPublisher(books.getBookPublisher());
            studyDto.setBookIsbn(books.getBookIsbn());
        } else if (studyDto.getStudyType().equals("LECTURE")) {
            studyDto.setLectureName(lectures.getLectureName());
            studyDto.setLecturer(lectures.getLecturer());
            studyDto.setLectureNumber(lectures.getLectureNumber());
        }

        return studyDto;
    }

    public void updateStudyStatusRecruitComplete(Study study) {
        study.setStudyStatus(StudyStatus.RECRUIT_COMPLETE);
        studyRepository.save(study);
    }

    public List<Study> findAllByStudyTitleContainsOrUser_userIdContains(String kw) {
        return studyRepository.findAllByStudyTitleContainsOrUser_userIdContains(kw, kw);
    }

    public List<Study> getList(String st, String ss, String kw) {

        if (st.equals("ALL")) {
            if (ss.equals("ALL")) {
                return studyRepository.findAllByStudyTitleContainsOrUser_userIdContains(kw, kw);
            }
            else {
                return studyRepository.findAllByStudyTitleContainsAndStudyStatusOrUser_userIdContainsAndStudyStatus(kw, StudyStatus.valueOf(ss), kw, StudyStatus.valueOf(ss));
            }
        }
        else {
            if(ss.equals("ALL")) {
                return studyRepository.findAllByUser_userIdContainsAndStudyTypeOrStudyTitleContainsAndStudyType(kw, StudyType.valueOf(st), kw, StudyType.valueOf(st));
            }
            else {
                return studyRepository.findAllByStudyTypeAndStudyStatusAndStudyTitleContainsOrStudyTypeAndStudyStatusAndUser_userIdContains(StudyType.valueOf(st), StudyStatus.valueOf(ss),kw, StudyType.valueOf(st), StudyStatus.valueOf(ss), kw);
            }
        }
    }

    /*
    매일 12시 스터디 상태 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStudyStatus() {
        List<Study> studyList = studyRepository.findAll();
        for (Study study : studyList) {
            int startYear = Integer.parseInt(study.getStudyStart().substring(0, 4));
            int startMonth = Integer.parseInt(study.getStudyStart().substring(5, 7));
            int startDay = Integer.parseInt(study.getStudyStart().substring(8, 10));

            int endYear = Integer.parseInt(study.getStudyEnd().substring(0, 4));
            int endMonth = Integer.parseInt(study.getStudyEnd().substring(5, 7));
            int endDay = Integer.parseInt(study.getStudyEnd().substring(8, 10));

            if (LocalDate.now().isEqual(LocalDate.of(startYear, startMonth, startDay))) {
                study.setStudyStatus(StudyStatus.PROGRESS);
            }

            if (LocalDate.now().isAfter(LocalDate.of(endYear, endMonth, endDay))) {
                study.setStudyStatus(StudyStatus.COMPLETE);
            }
            studyRepository.save(study);
        }
    }
}
