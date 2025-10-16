package com.signalmatch_backend.match.dto;

import com.signalmatch_backend.match.domain.enums.MatchReasonCode;

import java.time.LocalDateTime;

public record MatchCanceledEvent(
        Long matchId,
        Long startupId,
        Long investorId,
        String status,
        LocalDateTime canceledAt,
        MatchReasonCode reasonCode,
        String reasonText
) {
}
