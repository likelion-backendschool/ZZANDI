package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository {
    Study save(Study study);

    Optional<Study> findById(Long id);

    Optional<Study> findBytitle(String title);

    Optional<Study> findStudyPeople(int people);

    Optional<Study> findBystartDate(Date startDate);

    List<Study> findAll();

    Study delete(Study studies, Long studyId);

}
