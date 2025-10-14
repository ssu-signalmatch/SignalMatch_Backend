package com.signalmatch_backend.match.dto;

import java.time.LocalDateTime;

public record MatchAcceptedEvent(
        Long matchId,
        Long startupId,
        Long investorId,
        String status,
        LocalDateTime acceptedAt
) {}
