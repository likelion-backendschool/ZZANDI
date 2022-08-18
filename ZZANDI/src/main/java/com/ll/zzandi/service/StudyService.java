package com.ll.zzandi.service;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.dto.StudyDto;
import com.ll.zzandi.exception.StudyForm;
import com.ll.zzandi.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class StudyService {

    private final StudyRepository studyRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void save(@Valid StudyForm studyform) {
        Study s1 = new Study();
        s1.setStudyTitle(studyform.getStudyTitle());
        s1.setStudyStart(studyform.getStudyStart());
        s1.setStudyEnd(studyform.getStudyEnd());
        s1.setStudyPeople(studyform.getStudyPeople());
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

    public void modify(Long studyId, @Valid StudyForm studyform) {
        Study s1 = new Study();
        s1.setId(studyId);
        s1.setStudyTitle(studyform.getStudyTitle());
        s1.setStudyStart(studyform.getStudyStart());
        s1.setStudyEnd(studyform.getStudyEnd());
        s1.setStudyPeople(studyform.getStudyPeople());
        studyRepository.save(s1);
    }

}
