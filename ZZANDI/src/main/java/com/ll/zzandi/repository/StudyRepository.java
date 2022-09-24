package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select s from Study s where (s.studyTitle like %:kw% or s.user.userNickname like %:kw%) "
        + "and (:st is null or s.studyType = :st) and ((:ss is null or s.studyStatus = :ss)) and (:tag is null or s.studyTag = :tag)",
    countQuery = "select count(s) from Study s where (s.studyTitle like %:kw% or s.user.userNickname like %:kw%) "
        + "and (:st is null or s.studyType = :st) and ((:ss is null or s.studyStatus = :ss)) and (:tag is null or s.studyTag = :tag)")
    Page<Study> searchByKwAndOption(@Param("kw") String kw, @Param("st") StudyType st, @Param("ss") StudyStatus ss, @Param("tag") String tag, Pageable paging);

    @Query("select s from Study s join s.teamMateList teamMate where teamMate.user.id = :id")
    List<Study> findMyStudyList(@Param("id") Long id, Pageable pageable);

    @Query("select s from Study s")
    List<Study> findNewStudyList(Pageable pageable);

    @Query("select s from Study s where (:tag is null or s.studyTag = :tag)")
    List<Study> findFieldStudyList(@Param("tag") String tag, PageRequest paging);

    @Query("select s from Study s where (:interest1 is null or s.studyTag = :interest1) or (:interest2 is null or s.studyTag = :interest2) or (:interest3 is null or s.studyTag = :interest3)")
    List<Study> findInterestStudyList(@Param("interest1") String interest1, @Param("interest2") String interest2, @Param("interest3") String interest3, PageRequest paging);

    @Query("select s from Study s where (:interest is null or s.studyTag = :interest)")
    List<Study> findInterest1StudyList(@Param("interest") String interest, PageRequest paging);
}
