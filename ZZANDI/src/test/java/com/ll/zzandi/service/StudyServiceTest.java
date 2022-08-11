package com.ll.zzandi.service;
import com.ll.zzandi.domain.Study;
import com.ll.zzandi.repository.MemoryStudyRepository;
import com.ll.zzandi.repository.StudyRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
class StudyServiceTest {

    StudyService studyService;
    MemoryStudyRepository studyRepository;

    @BeforeEach
    public void beforeEach(){
        studyRepository = new MemoryStudyRepository();
        studyService = new StudyService(studyRepository);
    }

    @AfterEach
    public void afterEach(){
        studyRepository.clearStore();
    }


    @Test
    void 스터디생성() {
        //given
        Study study = new Study();
        study.setStudyTitle("Spring");
        //when
        Long saveId = studyService.join(study);
        //then
        Study findStudy = studyService.findOne(saveId).get();
        assertThat(study.getStudyTitle()).isEqualTo(findStudy.getStudyTitle());
    }

    @Test
    public void 중복_스터디명_예외(){
        //given
        Study study1 = new Study();
        study1.setStudyTitle("Spring");

        Study study2 = new Study();
        study2.setStudyTitle("Spring");
        //when
        studyService.join(study1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> studyService.join(study2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 스터디명입니다.");
        //then

    }

    @Test
    void findStudies() {
    }

    @Test
    void findOne() {
    }
}