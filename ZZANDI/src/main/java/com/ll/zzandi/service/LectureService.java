package com.ll.zzandi.service;
import com.ll.zzandi.domain.Lecture;
import com.ll.zzandi.dto.LectureDto;
import com.ll.zzandi.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    public Lecture save(LectureDto lectureDto) {
        Lecture l1 = new Lecture();
        l1.setLectureNumber(lectureDto.getLectureNumber());
        l1.setLecturer(lectureDto.getLecturer());
        l1.setLectureName(lectureDto.getLectureName());
        return lectureRepository.save(l1);
    }

    public Optional<Lecture> findById(Long studyId) {
        return lectureRepository.findById(studyId);
    }

    public Lecture modify(Long lectureId, LectureDto lectureDto) {
        Lecture l1 = new Lecture();
        l1.setId(lectureId);
        l1.setLectureName(lectureDto.getLectureName());
        l1.setLecturer(lectureDto.getLecturer());
        l1.setLectureNumber(lectureDto.getLectureNumber());
        return lectureRepository.save(l1);
    }

    public void delete(Lecture lectures) {
        lectureRepository.delete(lectures);
    }
}
