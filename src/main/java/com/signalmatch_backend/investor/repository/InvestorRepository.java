package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor,Long> {
    boolean existsByOwner(User owner);

}
