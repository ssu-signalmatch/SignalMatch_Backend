package com.signalmatch_backend.startup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record StartupProfileUpdateRequest(

    @Schema(description = "회사 이름")
    String startupName,

    @Schema(description = "운영여부")
    String status,

    @Schema(description = "설립일")
    LocalDate foundingDate,

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
    List<String> businessAreas
) {

}
