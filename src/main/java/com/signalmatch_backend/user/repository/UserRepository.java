package com.signalmatch_backend.user.repository;

import com.signalmatch_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByLoginId(String LoginId);
}
