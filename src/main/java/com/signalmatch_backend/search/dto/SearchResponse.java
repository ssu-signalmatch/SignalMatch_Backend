package com.signalmatch_backend.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SearchResponse(
    List<StartupSearch> startups,
    long totalStartupCount,
    int totalStartupPages,
    List<InvestorSearch> investors,
    long totalInvestorCount,
    int totalInvestorPages

) {

}
