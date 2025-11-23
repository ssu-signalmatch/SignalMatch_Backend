package com.signalmatch_backend.chat.dto;

import com.signalmatch_backend.chat.domain.enums.ChatSenderRole;

public record ChatMessageRequest(
        ChatSenderRole senderRole,  // STARTUP / INVESTOR
        Long senderId,              // 실제 주체 ID (startupId or investorId)
        String content
) {}
