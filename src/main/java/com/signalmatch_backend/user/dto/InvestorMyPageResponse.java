package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.investor.dto.InvestorProfileInfo;

public record InvestorMyPageResponse(
        InvestorProfileInfo profile,
        int matchedStartupCount
) implements MyPageResponse{
    public static InvestorMyPageResponse of(InvestorProfileInfo profile, int matchedCount) {
        return new InvestorMyPageResponse(profile, matchedCount);
    }

}
