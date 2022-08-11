package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.DateUtil.now;

public class MemoryStudyRepositoryTest {

    MemoryStudyRepository repository = new MemoryStudyRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }


    @Test
    public void save(){
        Study study = new Study();
        study.setStudyTitle("test");

        repository.save(study);

        Study result = repository.findById(study.getId()).get();
        assertThat(study).isEqualTo(result);

    }

    @Test
    public void findByTitle(){
        Study study1 = new Study();
        study1.setStudyTitle("Study1");
        repository.save(study1);

        Study study2 = new Study();
        study2.setStudyTitle("Study2");
        repository.save(study2);


        Study result =  repository.findBytitle("Study1").get();

        assertThat(result).isEqualTo(study1);
    }

    @Test
    public void findAll(){
        Study study1 = new Study();
        study1.setStudyTitle("Study1");
        study1.setStudyPeople(3);
        repository.save(study1);

        Study study2 = new Study();
        study2.setStudyTitle("Study2");
        repository.save(study2);

        List<Study> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }
}
