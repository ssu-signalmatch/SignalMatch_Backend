package com.signalmatch_backend.startup.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    Optional<Startup> findByOwner(User user);
    boolean existsByOwner(User owner);
}
