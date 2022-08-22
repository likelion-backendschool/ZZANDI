package com.ll.zzandi.service;
import com.ll.zzandi.domain.Lecture;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void save(LectureDto lectureDto) {
        Lecture l1 = new Lecture();
        l1.setLectureNumber(lectureDto.getLectureNumber());
        l1.setLecturer(lectureDto.getLecturer());
        l1.setLectureName(lectureDto.getLectureName());
        lectureRepository.save(l1);
    }

    public Optional<Lecture> findById(Long studyId) {
        return lectureRepository.findById(studyId);
    }

    public void modify(Long studyId, LectureDto lectureDto) {
        Lecture l1 = new Lecture();
        l1.setId(studyId);
        l1.setLectureNumber(lectureDto.getLectureNumber());
        l1.setLecturer(lectureDto.getLecturer());
        l1.setLectureName(lectureDto.getLectureName());
        lectureRepository.save(l1);
    }

    public void delete(Lecture lectures) {
        lectureRepository.delete(lectures);
    }
}
