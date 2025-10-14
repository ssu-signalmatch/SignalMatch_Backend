package com.signalmatch_backend.match.repository;

import com.signalmatch_backend.match.domain.Match;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByStartupIdAndInvestorIdAndStatusIn(
            Long startupId, Long investorId, Collection<MatchStatus> statuses
    );
}
