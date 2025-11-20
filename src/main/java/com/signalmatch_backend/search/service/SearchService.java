package com.signalmatch_backend.search.service;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.investor.repository.InvestorPreferredAreaRepository;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.investor.repository.InvestorSpecification;
import com.signalmatch_backend.search.dto.InvestorSearch;
import com.signalmatch_backend.search.dto.SearchResponse;
import com.signalmatch_backend.search.dto.StartupSearch;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import com.signalmatch_backend.startup.repository.StartupBusinessAreaRepository;
import com.signalmatch_backend.startup.repository.StartupRepository;
import com.signalmatch_backend.startup.repository.StartupSpecification;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final InvestorPreferredAreaRepository investorPreferredAreaRepository;
    private final StartupBusinessAreaRepository startupBusinessAreaRepository;

    public SearchResponse search(String keyword, List<String> areas, Pageable pageable) {
        //스타트업
        Page<Startup> startupPage = startupRepository.findAll(
            StartupSpecification.search(keyword, areas),
            pageable
        );

        List<Startup> startups = startupPage.getContent();

        List<Long> startupIds = startups.stream()
            .map(Startup::getStartupId)
            .toList();

        List<StartupBusinessArea> allStartupAreas =
            startupBusinessAreaRepository.findAllByStartupIds(startupIds);

        Map<Long, List<StartupBusinessArea>> startupAreaMap = allStartupAreas.stream()
            .collect(Collectors.groupingBy(sba -> sba.getStartup().getStartupId()));

        List<StartupSearch> startupSearchList = startups.stream()
            .map(startup -> StartupSearch.from(
                startup,
                startupAreaMap.getOrDefault(startup.getStartupId(), Collections.emptyList())
            ))
            .toList();

        /////투자자
        Page<Investor> investorPage = investorRepository.findAll(
            InvestorSpecification.search(keyword, areas),
            pageable
        );

        List<Investor> investors = investorPage.getContent();

        List<InvestorSearch> investorSearchList;

        List<Long> investorIds = investors.stream()
            .map(Investor::getInvestorId)
            .toList();


        List<InvestorPreferredArea> allPreferredAreas =
            investorPreferredAreaRepository.findAllByInvestorIds(investorIds);

        Map<Long, List<InvestorPreferredArea>> investorAreaMap =
            allPreferredAreas.stream()
                .collect(Collectors.groupingBy(ipa -> ipa.getInvestor().getInvestorId()));


        investorSearchList = investors.stream()
                .map(investor -> InvestorSearch.from(
                    investor,
                    investorAreaMap.getOrDefault(investor.getInvestorId(), Collections.emptyList())
                ))
                .toList();

        return new SearchResponse(
                startupSearchList,
                startupPage.getTotalElements(),
                startupPage.getTotalPages(),
                investorSearchList,
                investorPage.getTotalElements(),
                investorPage.getTotalPages()
        );

    }
}
