package com.signalmatch_backend.match.repository;

import com.signalmatch_backend.match.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
