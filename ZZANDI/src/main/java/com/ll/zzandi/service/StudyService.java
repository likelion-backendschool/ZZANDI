package com.ll.zzandi.service;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.dto.StudyDto;
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

    public void save(StudyDto studyDto) {
        Study s1 = new Study();

        s1.setStudyTitle(studyDto.getStudyTitle());
        s1.setStudyStart(studyDto.getStudyStart());
        s1.setStudyEnd(studyDto.getStudyEnd());
        s1.setStudyPeople(studyDto.getStudyPeople());

        studyRepository.save(s1);
    }

    public List<Study> findall() {
        return studyRepository.findAll();
    }


    public Optional<Study> findByid(Long studyId) {
        return studyRepository.findById(studyId);
    }

    public void delete(Study studies) {
        studyRepository.delete(studies);
    }

}
