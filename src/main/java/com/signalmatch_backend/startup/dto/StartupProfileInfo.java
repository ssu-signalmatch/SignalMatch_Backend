package com.signalmatch_backend.startup.dto;

import com.signalmatch_backend.startup.domain.Startup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

public record StartupProfileInfo(
    @Schema(description = "회사 이름")
    String startupName,

    @Schema(description = "운영여부")
    String status,

    @Schema(description = "설립일")
    String foundingDate,

    @Schema(description = "주소")
    String address,

    @Schema(description = "url")
    String homepageUrl,

    @Email
    @Schema(description = "이메일")
    String contactEmail,

    @Schema(description = "기업소개")
    String intro,

    @Schema(description = "대표자명")
    String representativeName,

    @Schema(description = "사업자 번호")
    String businessNumber,

    @Schema(description = "고용인원")
    Integer employeeCount,

    @Schema(description = "상법상 분류")
    String legalType,

    @Schema(description = "규모 분류")
    String scale,

    @Schema(description = "매출액")
    Long revenue,

    @Schema(description = "영업이익")
    Long profit,

    @Schema(description = "투자유치건수")
    Integer fundingRounds,

    @Schema(description = "누적투자유치금액")
    Long totalFunding,

    @Schema(description = "투자 단계")
    String investorStages,

    @Schema(description = "산업분야")
    List<String> businessAreas,
    @Schema(description = "대표 약력")
    String history
) {
    public static StartupProfileInfo toStartupProfileInfo(Startup startup, List<String> startupBusinessAreas){
        return new StartupProfileInfo(
            startup.getStartupName(),
            startup.getStatus().name(),
            startup.getStartupProfile().getFoundingDate().toString(),
            startup.getStartupProfile().getAddress(),
            startup.getStartupProfile().getHomepageUrl(),
            startup.getStartupProfile().getContactEmail(),
            startup.getStartupProfile().getIntro(),
            startup.getStartupProfile().getRepresentativeName(),
            startup.getStartupProfile().getBusinessNumber(),
            startup.getStartupProfile().getEmployeeCount(),
            startup.getStartupProfile().getLegalType().name(),
            startup.getStartupProfile().getScale().name(),
            startup.getStartupFinance().getRevenue(),
            startup.getStartupFinance().getProfit(),
            startup.getStartupFinance().getFundingRounds(),
            startup.getStartupFinance().getTotalFunding(),
            startup.getStartupFinance().getInvestorStages().name(),
            startupBusinessAreas,
            startup.getStartupProfile().getHistory()
        );
    }
}
