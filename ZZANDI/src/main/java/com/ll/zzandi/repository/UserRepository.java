package com.ll.zzandi.repository;

import com.ll.zzandi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    User findByUserEmail(String email);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserNickname(String userNickname);

    boolean existsByUserId(String userid);
}
