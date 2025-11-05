package com.signalmatch_backend.s3.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "S3 Presigned URL 요청 DTO")
public record PresignRequest(
        @Schema(
                description = "업로드할 파일의 원본 이름 (확장자 포함)",
                example = "signalmatch.png"
        )
        String fileName,


        @Schema(
                description = "파일의 MIME 타입 (브라우저가 감지한 Content-Type)",
                example = "image/png or application/pdf"
        )
        String mimeType,

        @Schema(
                description = "파일 크기 (바이트 단위)",
                example = "34567"
        )
        Long contentLength
) {

}
