package com.signalmatch_backend.chat.dto;

public record ChatRoomCreateRequest(
        Long startupId,
        Long investorId
) {}