package com.signalmatch_backend.investor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record InvestorProfileCreateResponse(
    @Schema(description = "투자자 ID")
    Long investorId

) {
}
