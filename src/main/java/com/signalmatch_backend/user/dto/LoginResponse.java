package com.signalmatch_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "jwt 액세스 토큰")
        String accessToken,
        long userId
) {
}
