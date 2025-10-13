package com.signalmatch_backend.match.dto;

import java.time.LocalDateTime;

public record MatchRequestedEvent(
        Long matchId,
        Long startupId,
        Long investorId,
        String status,              // "REQUESTED" 등
        LocalDateTime requestedAt
) {
}
