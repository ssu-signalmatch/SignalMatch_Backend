package com.signalmatch_backend.investor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record InvestorProfileUpdateRequest(
    @Schema(description = "투자자(기업) 이름")
    String investorName,
    @Schema(description = "투자자 이메일")
    String contactEmail,
    @Schema(description = "연락처")
    String phoneNumber,
    @Schema(description = "url")
    String websiteUrl,
    @Schema(description = "한줄소개")
    String intro,
    @Schema(description = "소속")
    String organizationName,
    @Schema(description = "투자유형")
    String investorType,
    @Schema(description = "투자희망규모")
    String preferredInvestmentSize,
    @Schema(description = "투자단계")
    List<String> preferredStages,

    @Schema(description = "선호 산업분야")
    List<String> preferredAreas
) {
}
