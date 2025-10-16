package com.signalmatch_backend.investor.dto;

import com.signalmatch_backend.investor.domain.Investor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record InvestorProfileInfo(
    @Schema(description = "투자자 ID")
    Long investorId,
    @Schema(description = "조회수")
    Long views,
    @Schema(description = "투자자(기업) 이름")
    String investorName,
    @Schema(description = "투자자 이메일")
    String contactEmail,
    @Schema(description = "직함")
    String position,
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
    public static InvestorProfileInfo toInvestorProfileInfo(Investor investor,
        List<String> preferredAreas) {
        return new InvestorProfileInfo(
            investor.getInvestorId(),
            investor.getViews(),
            investor.getInvestorName(),
            investor.getContactEmail(),
            investor.getPosition(),
            investor.getPhoneNumber(),
            investor.getWebsiteUrl(),
            investor.getIntro(),
            investor.getOrganizationName(),
            investor.getInvestorType().name(),
            investor.getPreferredInvestmentSize().name(),
            investor.getPreferredStages().stream()
                .map(stage -> stage.getId().getStageCode().name())
                .collect(Collectors.toList()),
            preferredAreas
        );
    }
}
