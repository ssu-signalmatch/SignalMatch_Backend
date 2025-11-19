package com.signalmatch_backend.startup.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StartupProfileCreateResponse(
    @Schema(description = "스타트업 ID")
    Long StartupId

) {
}
