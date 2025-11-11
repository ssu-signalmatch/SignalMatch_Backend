package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.investor.dto.InvestorProfileInfo;

public record InvestorMyPageResponse(
        InvestorProfileInfo profile,
        long matchedStartupCount,
        String profileImageUrl
) implements MyPageResponse{
    public static InvestorMyPageResponse of(InvestorProfileInfo profile, long matchedCount,String profileImageUrl) {
        return new InvestorMyPageResponse(profile, matchedCount, profileImageUrl);
    }

}
