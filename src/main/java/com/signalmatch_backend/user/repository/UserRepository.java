package com.signalmatch_backend.user.repository;

import com.signalmatch_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByLoginId(String LoginId);

    Optional<User> findByLoginId(String loginId);
}
