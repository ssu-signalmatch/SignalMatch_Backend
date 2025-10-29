package com.signalmatch_backend.match.repository;

import com.signalmatch_backend.match.domain.Match;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByStartupIdAndInvestorIdAndStatusIn(
            Long startupId, Long investorId, Collection<MatchStatus> statuses
    );

    List<Match> findAllByStartupId(Long startupId);
    List<Match> findAllByInvestorId(Long investorId);

    long countByStartupIdAndStatus(Long startupId, MatchStatus status);
    long countByInvestorIdAndStatus(Long investorId, MatchStatus status);
}
