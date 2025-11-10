package com.signalmatch_backend.document.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문서 단건 응답 DTO")
public record DocumentResponse(
        @Schema(description = "문서 ID", example = "123")
        Long documentId,

        @Schema(description = "S3 object key", example = "companies/7/ir/2025/11/10/uuid.pdf")
        String objectKey,

        @Schema(description = "CloudFront를 통한 접근 URL", example = "https://d1rk1hmjfxzinj.cloudfront.net/companies/7/ir/2025/11/10/uuid.pdf")
        String url,

        @Schema(description = "파일명(추정, key의 마지막 세그먼트)", example = "uuid.pdf")
        String fileName
) {}
