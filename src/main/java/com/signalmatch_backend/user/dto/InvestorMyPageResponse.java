package com.signalmatch_backend.user.dto;

import com.signalmatch_backend.investor.dto.InvestorProfileInfo;

public record InvestorMyPageResponse(
        InvestorProfileInfo profile,
        long bookmarkCount,
        String profileImageUrl,
        String updatedAt
) implements MyPageResponse{
    public static InvestorMyPageResponse of(InvestorProfileInfo profile, long bookmarkCount,String profileImageUrl,String updatedAt) {
        return new InvestorMyPageResponse(profile, bookmarkCount, profileImageUrl,updatedAt);
    }

}
