package com.ll.zzandi.service;

import com.ll.zzandi.domain.*;
import com.ll.zzandi.dto.BookDto;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.dto.study.StudyDetailDto;
import com.ll.zzandi.dto.study.StudyListDto;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.StudyException;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.BookRepository;
import com.ll.zzandi.repository.CommentRepository;
import com.ll.zzandi.repository.StudyRepository;
import com.ll.zzandi.repository.TeamMateRepository;
import com.ll.zzandi.enumtype.TableType;
import com.ll.zzandi.repository.*;

import java.io.IOException;
import java.time.Period;

import com.ll.zzandi.util.aws.ImageUploadService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final BookService bookService;
    private final LectureService lectureService;
    private final StudyRepository studyRepository;
    private final BookRepository bookRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ImageUploadService imageUploadService;
    private final FileRepository fileRepository;
    private final InterestRepository interestRepository;
    private final TeamMateRepository teamMateRepository;

    public Study createStudyWithBook(@Valid StudyDto studyDto, Book book, User user) {
        Study study = new Study(user, studyDto.getStudyTitle(), book, null, StudyType.BOOK,
            studyDto.getStudyStart(), studyDto.getStudyEnd(), studyDto.getStudyPeople(),
            studyDto.getStudyTag(), StudyStatus.RECRUIT);

        study = study.checkStatus();
        study.setStudyCoverUrl("https://cdn-icons-png.flaticon.com/512/4683/4683425.png");
        return studyRepository.save(study);
    }

    public Study createStudyWithLecture(@Valid StudyDto studyDto, Lecture lecture, User user) {
        Study study = new Study(user, studyDto.getStudyTitle(), null, lecture, StudyType.LECTURE,
            studyDto.getStudyStart(), studyDto.getStudyEnd(), studyDto.getStudyPeople(),
            studyDto.getStudyTag(), StudyStatus.RECRUIT);
        study = study.checkStatus();
        study.setStudyCoverUrl("https://cdn-icons-png.flaticon.com/512/2112/2112961.png");
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

    public void updateStudyWithBook(Long studyId, @Valid StudyDto studyDto, BookDto bookDto, User user) {
        Study s1 = studyRepository.findById(studyId).orElseThrow(null);
        Book book = bookService.modify(s1.getBook().getId(), bookDto);

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
        s1 = s1.checkStatus();
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
        s1 = s1.checkStatus();
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

    public Page<StudyListDto> findStudyListPaging(String st, String ss, String tag, String kw, int page) {
        PageRequest paging = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "id"));

        if (st.equals("ALL")) st = null;
        if (ss.equals("ALL")) ss = null;
        if (tag.equals("ALL")) tag = null;

        StudyType stObj = null;
        StudyStatus ssObj = null;
        if (st != null) {
            stObj = StudyType.valueOf(st);
        }
        if (ss != null) {
            ssObj = StudyStatus.valueOf(ss);
        }

        Page<Study> studyList = studyRepository.searchByKwAndOption(kw, stObj, ssObj, tag, paging);
        return studyList.map(
            study -> new StudyListDto(study.getId(), study.getStudyTitle(), study.getAcceptedStudyMember(), study.getStudyPeople(),
                study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
                String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
                String.valueOf(study.getStudyStatus())));
    }

    public void updateCoverImg(MultipartFile multipartFile, long studyUUID) throws IOException {
        Study study=studyRepository.findById(studyUUID).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));

        String originalName=multipartFile.getOriginalFilename();
        System.out.println("!!!!!!!!!!!!!!!오리지널"+originalName);
        String[] name=originalName.split("\\\\");
        final String ext = name[2].substring(name[2].lastIndexOf('.'));
        final String saveFileName = getUuid() + ext;
        String studyCoverUrl=imageUploadService.upload(saveFileName,multipartFile);
        File file=File.builder()
                .fileName(saveFileName)
                .originalName(name[2])
                .fileExt(ext)
                .fileSize(multipartFile.getSize())
                .fileUrl(studyCoverUrl)
                .tableId(studyUUID)
                .tableType(TableType.STUDY)
                .build();
        file.deleteFileStatus();
        fileRepository.save(file);
        study.getStudyCoverUrl(studyCoverUrl);
    }
    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void updateRecruitStudyStatus(Study study) {
        if(study.getAcceptedStudyMember() == study.getStudyPeople()) {
            study.setStudyStatus(StudyStatus.RECRUIT_COMPLETE);
            studyRepository.save(study);
        }else if(study.getAcceptedStudyMember() < study.getStudyPeople()) {
            study.setStudyStatus(StudyStatus.RECRUIT);
            studyRepository.save(study);
        }
    }

    public StudyDetailDto findStudyDetail(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));
        Book book = study.getBook();
        Lecture lecture = study.getLecture();
        StudyDetailDto studyDetailDto = null;
        if (book != null) {
            book = bookService.findByid(book.getId()).orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));
            studyDetailDto = new StudyDetailDto(study.getStudyTitle(), study.getUser().getUserNickname(),
                study.getAcceptedStudyMember(), study.getStudyPeople(), study.getStudyStart(), study.getStudyEnd(),
                study.getStudyTag(), String.valueOf(study.getStudyType()), study.getStudyRate(), study.getViews(),
                study.getStudyCoverUrl(), String.valueOf(study.getStudyStatus()), study.getRecommend(),
                book.getBookName(), book.getBookPage(), book.getBookAuthor(), book.getBookPublisher(), book.getBookIsbn(),
                null, null, null);
        } else if (lecture != null) {
            lecture = lectureService.findById(lecture.getId()).orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));
            studyDetailDto = new StudyDetailDto(study.getStudyTitle(), study.getUser().getUserNickname(),
                study.getAcceptedStudyMember(), study.getStudyPeople(), study.getStudyStart(), study.getStudyEnd(),
                study.getStudyTag(), String.valueOf(study.getStudyType()), study.getStudyRate(), study.getViews(),
                study.getStudyCoverUrl(), String.valueOf(study.getStudyStatus()), study.getRecommend(),
                null, null, null, null, null,
                lecture.getLectureName(), lecture.getLecturer(), lecture.getLectureNumber());
        }

        return studyDetailDto;
    }

    /*
    매일 12시 스터디 상태 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStudyStatus() {
        List<Study> studyList = studyRepository.findAll();
        for (Study study : studyList) {
            study = study.checkStatus();
            studyRepository.save(study);

            if (study.getStudyStatus().equals(StudyStatus.PROGRESS)) {
                updateStudy(study);
            }
        }
    }
    public void updateStudy(Study study) {

        int total = (study.getStudyType().equals(StudyType.BOOK))
            ? study.getBook().getBookPage() : study.getLecture().getLectureNumber();

        int studyLeftNum = total - study.getStudyRate();

        int studyDays = getStudyDays(study.getId());

        int recommend = 0;

        if (studyLeftNum % studyDays > 0) {
            recommend = (int) Math.ceil(total / studyDays) + 1;
        }
        else {
            recommend = (int) Math.ceil(total / studyDays);
        }

        study.setStudyRate(study.getStudyRate() + recommend);
        study.setRecommend(recommend);

        studyRepository.save(study);
    }

    public int getStudyPeriod(Long studyId) {// 전체 스터디 기간을 return
        Study studies = findByStudyId(studyId).orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));

        LocalDate studyStart = LocalDate.parse(studies.getStudyStart());
        LocalDate studyEnd= LocalDate.parse(studies.getStudyEnd());

        return Period.between(studyStart, studyEnd).getDays();
    }

    public int getStudyDays(Long studyId) { // 남은 스터디 기간을 return
        Study studies = findByStudyId(studyId).orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));

        LocalDate studyEnd = LocalDate.parse(studies.getStudyEnd());
        LocalDate today = LocalDate.now();

        return Period.between(today, studyEnd).getDays() + 1;
    }

    public void updateViews(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));
        study.setViews(study.getViews() + 1);
        studyRepository.save(study);
    }

    public List<StudyListDto> findMyStudyList(User user) {
        PageRequest paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));
        List<Study> studyList = studyRepository.findMyStudyList(user.getId(), paging);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }

    public List<StudyListDto> findNewStudyList() {
        PageRequest paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));
        List<Study> studyList = studyRepository.findNewStudyList(paging);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }

    public List<StudyListDto> findFieldStudyList(String tag) {
        PageRequest paging = PageRequest.of(0, 18, Sort.by(Sort.Direction.DESC, "id"));

        if (tag.equals("ALL")) tag = null;

        List<Study> studyList = studyRepository.findFieldStudyList(tag, paging);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }

    public List<StudyListDto> findInterestStudyList(User user) {
        PageRequest paging = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "id"));
        List<Interest> interestList = interestRepository.findByUser(user);
        Collections.shuffle(interestList);
        String interest1 = interestList.size() > 0 ? interestList.get(0).getInterest() : null;
        String interest2 = interestList.size() > 1 ? interestList.get(1).getInterest() : null;
        String interest3 = interestList.size() > 2 ? interestList.get(2).getInterest() : null;
//
//        List<Study> studyList = studyRepository.findInterestStudyList(interest1 , interest2, interest3, paging);

        List<Study> studyList1 = studyRepository.findInterest1StudyList(interest1 , paging);
        List<Study> studyList2 = studyRepository.findInterest1StudyList(interest2 , paging);
        paging = PageRequest.of(0, 18 - (studyList1.size() + studyList2.size()), Sort.by(Sort.Direction.DESC, "id"));
        List<Study> studyList3 = studyRepository.findInterest1StudyList(interest3 , paging);

        List<Study> studyList = new ArrayList<>();
        studyList.addAll(studyList1);
        studyList.addAll(studyList2);
        studyList.addAll(studyList3);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }

  public List<StudyListDto> findRecruitStudyList(User user) {
      PageRequest paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));

      List<Study> studyList = studyRepository.findByUserAndStudyStatusOrStudyStatus(user, StudyStatus.RECRUIT, StudyStatus.RECRUIT_COMPLETE, paging);

      return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
          study.getAcceptedStudyMember(), study.getStudyPeople(),
          study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
          String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
          String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
  }

    public List<StudyListDto> findProgressStudyList(User user) {
        PageRequest paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "id"));

        List<TeamMate> teamMateList = teamMateRepository.findAllByUser(user);
        List<Study> studyList = studyRepository.findByTeamMateListInAndStudyStatus(
            teamMateList, StudyStatus.PROGRESS, paging);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }

    public List<StudyListDto> findCompleteStudyList(User user) {
        PageRequest paging = PageRequest.of(0, 18, Sort.by(Sort.Direction.DESC, "id"));
        List<TeamMate> teamMateList = teamMateRepository.findAllByUser(user);
        List<Study> studyList = studyRepository.findByTeamMateListInAndStudyStatus(
            teamMateList, StudyStatus.COMPLETE, paging);

        return studyList.stream().map(study -> new StudyListDto(study.getId(), study.getStudyTitle(),
            study.getAcceptedStudyMember(), study.getStudyPeople(),
            study.getStudyStart(), study.getStudyEnd(), study.getStudyTag(),
            String.valueOf(study.getStudyType()), study.getViews(), study.getStudyCoverUrl(),
            String.valueOf(study.getStudyStatus()))).collect(Collectors.toList());
    }
}