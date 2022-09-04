package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import java.util.List;
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
}
