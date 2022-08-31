package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {
    Optional<Study> findById(Long id);

    List<Study> findAllByStudyTitleContainsOrUser_userIdContains(String kw1, String kw2);
}
