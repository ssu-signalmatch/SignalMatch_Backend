package com.signalmatch_backend.chat.dto;


public record ChatRoomResponse(
        Long roomId,
        Long startupId,
        Long investorId
) {}
