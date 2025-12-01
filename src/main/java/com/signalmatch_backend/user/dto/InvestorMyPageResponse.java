package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.investor.dto.InvestorProfileInfo;

public record InvestorMyPageResponse(
        InvestorProfileInfo profile,
        long bookmarkCount,
        String profileImageUrl
) implements MyPageResponse{
    public static InvestorMyPageResponse of(InvestorProfileInfo profile, long bookmarkCount,String profileImageUrl) {
        return new InvestorMyPageResponse(profile, bookmarkCount, profileImageUrl);
    }

}
