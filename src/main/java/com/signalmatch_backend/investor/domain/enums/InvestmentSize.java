package com.signalmatch_backend.investor.domain.enums;

public enum InvestmentSize {
    UNDER_50M,        // 5천만 미만
    SIZE_50M_TO_100M, // 5천만~1억
    SIZE_100M_TO_1B,  // 1억~10억
    SIZE_1B_TO_10B,   // 10억~100억
    OVER_10B          // 100억 이상
}