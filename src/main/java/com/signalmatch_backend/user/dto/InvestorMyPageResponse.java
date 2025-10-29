package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.investor.dto.InvestorProfileInfo;

public record InvestorMyPageResponse(
        InvestorProfileInfo profile,
        long matchedStartupCount
) implements MyPageResponse{
    public static InvestorMyPageResponse of(InvestorProfileInfo profile, long matchedCount) {
        return new InvestorMyPageResponse(profile, matchedCount);
    }

}
