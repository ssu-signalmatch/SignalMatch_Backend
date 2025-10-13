package com.signalmatch_backend.match.dto;

public record MatchCreateRequest(
        Long investorId,
        Long startupId
) {
}
