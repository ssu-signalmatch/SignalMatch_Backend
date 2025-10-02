package com.signalmatch_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequest(
        @Schema(description = "유저 아이디")
        String loginId,
        @Schema(description = "비밀번호")
        String password,
        @Schema(description = "유저 이름")
        String name
){
}
