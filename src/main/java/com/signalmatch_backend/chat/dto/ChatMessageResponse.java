package com.signalmatch_backend.chat.dto;

import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.user.domain.enums.UserRole;

import java.time.LocalDateTime;


public record ChatMessageResponse(
        Long id,
        Long chatRoomId,
        Long startupId,
        Long investorId,
        UserRole senderRole,   // STARTUP / INVESTOR
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
                m.getSenderRole(),        // ChatMessage.senderRole 타입을 UserRole 로 두는 걸 추천
                m.getSenderId(),
                m.getContent(),
                m.getCreatedAt(),
                m.isDeleted()
        );
    }
}