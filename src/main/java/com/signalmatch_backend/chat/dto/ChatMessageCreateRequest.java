package com.signalmatch_backend.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageCreateRequest(
        @NotBlank(message = "메시지 내용은 필수입니다.")
        String content
) {}
