package com.signalmatch_backend.s3.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;


@Schema(description = "S3 Presigned URL 응답 DTO")
public record PresignResponse(

        @Schema(
                description = "S3로 직접 업로드할 presigned URL",
                example = "https://your-bucket.s3.ap-northeast-2.amazonaws.com/users/1/profile/uuid.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&..."
        )
        String url,

        @Schema(
                description = "요청 메서드 (PUT 또는 POST)",
                example = "PUT"
        )
        String method,

        @Schema(
                description = "S3 업로드 시 함께 포함해야 하는 헤더들",
                example = "{\"host\": \"your-bucket.s3.ap-northeast-2.amazonaws.com\"}"
        )
        Map<String, String> headers,


        @Schema(
                description = "S3 object key (업로드 완료 후 서버에 저장할 경로)",
                example = "users/1/profile/uuid.png"
        )
        String key
) {
}
