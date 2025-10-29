package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.startup.domain.StartupProfile;
import com.signalmatch_backend.startup.dto.StartupProfileInfo;

public record StartupMyPageResponse(
        StartupProfileInfo profile,
        long matchedInvestorCount
) implements MyPageResponse {
    public static StartupMyPageResponse of(StartupProfileInfo profile, long matchedCount) {
        return new StartupMyPageResponse(profile, matchedCount);
    }
}
