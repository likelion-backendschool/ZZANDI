package com.ll.zzandi.repository;
import com.ll.zzandi.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    Optional<Lecture> findById(Long id);
}
