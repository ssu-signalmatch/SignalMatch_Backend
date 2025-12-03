package com.signalmatch_backend.search.dto;

import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import java.util.List;

public record StartupSearch(
    String userId,
    String startupId,
    String startupName,
    String intro,
    String legalType,
    String scale,
    String investorStages,
    List<String> businessAreas
) {
    public static StartupSearch from(Startup startup, List<StartupBusinessArea> areas) {

        List<String> areaNames = areas.stream()
            .filter(a -> a.getStartup().getStartupId().equals(startup.getStartupId()))
            .map(a -> a.getBusinessArea().getName())
            .toList();

        return new StartupSearch(
            startup.getOwner().getUserId().toString(),
            startup.getStartupId().toString(),
            startup.getStartupName(),
            startup.getStartupProfile().getIntro(),
            startup.getStartupProfile().getLegalType().name(),
            startup.getStartupProfile().getScale().name(),
            startup.getStartupFinance().getInvestorStages().name(),
            areaNames
        );
    }
}
