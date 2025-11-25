package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestorRepository extends JpaRepository<Investor,Long> ,JpaSpecificationExecutor<Investor> {
    boolean existsByOwner(User owner);
    Optional<Investor> findByOwner(User owner);
}
