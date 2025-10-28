package com.signalmatch_backend.startup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record StartupProfileCreateRequest(
    @NotBlank
    @Schema(description = "회사 이름")
    String startupName,
    @NotBlank
    @Schema(description = "운영여부")
    String status,
    @NotNull
    @Schema(description = "설립일")
    LocalDate foundingDate,
    @NotBlank
    @Schema(description = "주소")
    String address,
    @NotBlank
    @Schema(description = "url")
    String homepageUrl,
    @NotBlank
    @Email
    @Schema(description = "이메일")
    String contactEmail,
    @NotBlank
    @Schema(description = "기업소개")
    String intro,
    @NotBlank
    @Schema(description = "대표자명")
    String representativeName,
    @NotBlank
    @Schema(description = "사업자 번호")
    String businessNumber,
    @NotNull
    @Schema(description = "고용인원")
    Integer employeeCount,
    @NotBlank
    @Schema(description = "상법상 분류")
    String legalType,
    @NotBlank
    @Schema(description = "규모 분류")
    String scale,
    @NotNull
    @Schema(description = "매출액")
    Long revenue,
    @NotNull
    @Schema(description = "영업이익")
    Long profit,
    @NotNull
    @Schema(description = "투자유치건수")
    Integer fundingRounds,
    @NotNull
    @Schema(description = "누적투자유치금액")
    Long totalFunding,
    @NotBlank
    @Schema(description = "투자 단계")
    String investorStages,
    @NotEmpty
    @Schema(description = "산업분야")
    List<String> businessAreas
) {
}
