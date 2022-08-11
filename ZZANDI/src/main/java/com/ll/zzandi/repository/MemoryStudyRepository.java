package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class MemoryStudyRepository implements StudyRepository{

    private static Map<Long, Study> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Study save(Study study) {
        study.setId(++sequence);
        store.put(study.getId(), study);
        return study;
    }

    @Override
    public Optional<Study> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Study> findBytitle(String title) {
        return store.values().stream()
                .filter(study -> study.getStudyTitle().equals(title))
                .findAny();
    }
    @Override
    public Optional<Study> findStudyPeople(int people) {
        return Optional.ofNullable(store.get(people));
    }
    @Override
    public Optional<Study> findBystartDate(Date startDate) {
        return store.values().stream()
                .filter(study -> study.getStudyStart().equals(startDate))
                .findAny();
    }

    @Override
    public List<Study> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Study delete(Study studies, Long studyId) {
        studies.setId(studyId);
        store.remove(studies.getId(),studies);
        return studies;
    }


    public void clearStore(){
        store.clear();
    }
    
   
}
