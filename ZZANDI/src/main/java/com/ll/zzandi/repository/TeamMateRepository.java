package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMateRepository extends JpaRepository<TeamMate, Long> {

  Optional<TeamMate> findByUserAndAndStudy(User currentUser, Study study);

  List<TeamMate> findAllByUser(User user);

  List<TeamMate> findByStudy(Study study);
}
