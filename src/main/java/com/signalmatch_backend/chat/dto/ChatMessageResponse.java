package com.signalmatch_backend.chat.dto;

import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.user.domain.enums.UserRole;

import java.time.LocalDateTime;


public record ChatMessageResponse(
        Long id,
        Long chatRoomId,
        UserRole senderRole,
        Long senderId,
        String content,
        boolean deleted
) {
    public static ChatMessageResponse from(ChatMessage m) {
        return new ChatMessageResponse(
                m.getId(),
                m.getChatRoom().getId(),
                m.getSenderRole(),
                m.getSenderId(),
                m.getContent(),
                m.isDeleted()
        );
    }
}