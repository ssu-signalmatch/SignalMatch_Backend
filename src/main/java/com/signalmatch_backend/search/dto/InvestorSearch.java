package com.signalmatch_backend.search.dto;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import java.util.List;

public record InvestorSearch(
    String investorName,
    String intro,
    String organizationName,
    String investorType,
    List<String> preferredAreas
) {
    public static InvestorSearch from(Investor investor, List<InvestorPreferredArea> preferredAreas) {

        List<String> areaNames = preferredAreas.stream()
            .filter(a -> a.getInvestor().getInvestorId().equals(investor.getInvestorId()))
            .map(a -> a.getBusinessArea().getName())
            .toList();

        return new InvestorSearch(
            investor.getInvestorName(),
            investor.getIntro(),
            investor.getOrganizationName(),
            investor.getInvestorType().name(),
            areaNames
        );
    }
}
