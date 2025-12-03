package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.startup.domain.StartupProfile;
import com.signalmatch_backend.startup.dto.StartupProfileInfo;

public record StartupMyPageResponse(
        StartupProfileInfo profile,
        long bookmarkCount,
        String profileImageUrl,
        String updatedAt
) implements MyPageResponse {
    public static StartupMyPageResponse of(StartupProfileInfo profile, long bookmarkCount,String profileImageUrl,String updatedAt) {
        return new StartupMyPageResponse(profile, bookmarkCount, profileImageUrl, updatedAt);
    }
}
