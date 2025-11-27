package com.signalmatch_backend.chat.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomListResponse(
        Long roomId,
        Long opponentId,
        String opponentName,
        String opponentProfileImage,
        String lastMessage,
        LocalDateTime lastMessageTime,
        boolean hasUnread
) {}