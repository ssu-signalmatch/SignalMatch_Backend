package com.signalmatch_backend.match.dto;

import jakarta.validation.constraints.NotNull;

public record MatchCreateRequest(
        @NotNull Long investorId,
        @NotNull Long startupId
) {
}
