//
//import com.ll.zzandi.domain.Study;
//import com.ll.zzandi.domain.User;
//import com.ll.zzandi.dto.StudyRequestDto;
//import com.ll.zzandi.exception.ErrorCode;
//import com.ll.zzandi.exception.UserApplicationException;
//import com.ll.zzandi.repository.StudyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//
//    private final StudyRepository studyRepository;
//
//    @Transactional
//    public Study study(final StudyRequestDto studyRequestDto) {
//        return studyRepository.save(Study.of(studyRequestDto));
//    }
//}
//
//


package com.ll.zzandi.service;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.repository.MemoryStudyRepository;
import com.ll.zzandi.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyService {

    private final StudyRepository studyRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public Long join(Study study) {
        //같은 이름의 중복 스터디 X

        validateDuplicateStudy(study); //중복 스터디명 검증

        studyRepository.save(study);
        return study.getId();
    }

    private void validateDuplicateStudy(Study study) {
        studyRepository.findBytitle(study.getStudyTitle())
                .ifPresent(s -> {
                    throw new IllegalStateException("이미 존재하는 스터디명입니다.");
                });
    }

    public List<Study> findStudies() {
        return studyRepository.findAll();
    }

    public Optional<Study> findOne(Long studyId) {
        return studyRepository.findById(studyId);
    }

    public void delete(Study studies, Long studyId) {
        studyRepository.delete(studies, studyId);
    }

}

