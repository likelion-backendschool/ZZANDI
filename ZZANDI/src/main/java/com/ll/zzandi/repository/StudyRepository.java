package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {
    Optional<Study> findById(Long id);

    List<Study> findAllByStudyTitleContainsOrUser_userIdContains(String kw1, String kw2);
    @Override
    List<Study> findAll();
    List<Study> findAllByUser_userIdContainsAndStudyTypeOrStudyTitleContainsAndStudyType(String kw, StudyType valueOf,
        String kw1, StudyType valueOf1);

    List<Study> findAllByStudyTitleContainsAndStudyStatusOrUser_userIdContainsAndStudyStatus(String kw,
        StudyStatus valueOf, String kw1, StudyStatus valueOf1);

    List<Study> findAllByStudyTypeAndStudyStatusAndStudyTitleContainsOrStudyTypeAndStudyStatusAndUser_userIdContains(
        StudyType valueOf, StudyStatus valueOf1, String kw, StudyType valueOf2, StudyStatus valueOf3, String kw1);

    // 스터디 이름 & 스터디 팀장 검색
    Page<Study> findDistinctByStudyTitleContainsOrUser_userIdContains(String kw, String kw1, Pageable paging);

    // + StudyStatus(Book or Lecture) 검색
    Page<Study> findDistinctByStudyTitleContainsAndStudyStatusOrUser_userIdContainsAndStudyStatus(
        String kw, StudyStatus ss, String kw1, StudyStatus ss1,
        Pageable paging);

    // + StudyType(모집 중, 모집 완료) 검색
    Page<Study> findDistinctByStudyTitleContainsAndStudyTypeOrUser_userIdContainsAndStudyType(String kw,
        StudyType st, String kw1, StudyType st1, Pageable paging);

    // + StudyStatus && StudyType 검색
    Page<Study> findDistinctByStudyTypeAndStudyStatusAndStudyTitleContainsOrStudyTypeAndStudyStatusAndUser_userIdContains(
        StudyType st, StudyStatus ss, String kw, StudyType st1, StudyStatus ss1, String kw1, Pageable paging);
}
