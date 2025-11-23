package com.signalmatch_backend.chat.dto;

import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.domain.enums.ChatSenderRole;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        Long chatRoomId,
        Long startupId,
        Long investorId,
        ChatSenderRole senderRole,
        Long senderId,
        String content,
        LocalDateTime createdAt,
        boolean deleted
) {
    public static ChatMessageResponse from(ChatMessage m) {
        return new ChatMessageResponse(
                m.getId(),
                m.getChatRoom().getId(),
                m.getChatRoom().getStartupId(),
                m.getChatRoom().getInvestorId(),
                m.getSenderRole(),
                m.getSenderId(),
                m.getContent(),
                m.getCreatedAt(),
                m.isDeleted()
        );
    }
}