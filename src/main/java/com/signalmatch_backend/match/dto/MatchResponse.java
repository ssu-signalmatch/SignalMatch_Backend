package com.signalmatch_backend.match.dto;

import com.signalmatch_backend.match.domain.Match;
import lombok.Builder;

import java.math.MathContext;
import java.time.LocalDateTime;

@Builder
public record MatchResponse(
        Long matchId,
        Long startupId,
        Long investorId,
        String status,
        LocalDateTime matchedAt,
        LocalDateTime endedAt,
        String requestedBy
) {

    public static MatchResponse from(Match match) {
        return new MatchResponse(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                match.getMatchedAt(),
                match.getEndedAt(),
                match.getRequestedBy().name()
        );
    }
}
