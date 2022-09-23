package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Interest;
import com.ll.zzandi.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

  List<Interest> findByUser(User user);
}
