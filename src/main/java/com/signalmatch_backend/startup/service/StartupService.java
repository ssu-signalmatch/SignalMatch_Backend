package com.signalmatch_backend.startup.service;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.BusinessArea.repository.BusinessAreaRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import com.signalmatch_backend.startup.domain.StartupFinance;
import com.signalmatch_backend.startup.domain.StartupProfile;
import com.signalmatch_backend.startup.domain.enums.FundingStage;
import com.signalmatch_backend.startup.domain.enums.LegalType;
import com.signalmatch_backend.startup.domain.enums.ScaleType;
import com.signalmatch_backend.startup.domain.enums.StartupStatus;
import com.signalmatch_backend.startup.domain.key.StartupBusinessAreaKey;
import com.signalmatch_backend.startup.dto.StartupProfileCreateRequest;
import com.signalmatch_backend.startup.dto.StartupProfileCreateResponse;
import com.signalmatch_backend.startup.dto.StartupProfileInfo;
import com.signalmatch_backend.startup.dto.StartupProfileUpdateRequest;
import com.signalmatch_backend.startup.repository.StartupBusinessAreaRepository;
import com.signalmatch_backend.startup.repository.StartupRepository;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Star;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StartupService {
    private final UserFinder userFinder;
    private final StartupRepository startupRepository;
    private final BusinessAreaRepository businessAreaRepository;
    private final StartupBusinessAreaRepository startupBusinessAreaRepository;
    private final StartupFinder startupFinder;
    @Transactional
    public StartupProfileCreateResponse createStartupProfile(Long userId, StartupProfileCreateRequest request){
        User owner = userFinder.findByUserId(userId);
        if(startupRepository.existsByOwner(owner)){
            throw new CustomException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        Startup newStartup = Startup.builder()
            .owner(owner)
            .startupName(request.startupName())
            .status(StartupStatus.valueOf((request.status().toUpperCase())))
            .views(0L)
            .build();

        StartupProfile newStartupProfile = StartupProfile.builder()
            .startup(newStartup)
            .foundingDate(LocalDate.parse(request.foundingDate()))
            .address(request.address())
            .homepageUrl(request.homepageUrl())
            .contactEmail(request.contactEmail())
            .intro(request.intro())
            .representativeName(request.representativeName())
            .businessNumber(request.businessNumber())
            .employeeCount(request.employeeCount())
            .legalType(LegalType.valueOf(request.legalType().toUpperCase()))
            .scale(ScaleType.valueOf(request.scale().toUpperCase()))
            .history(request.history())
            .build();

        newStartup.addStartupProfile(newStartupProfile);

        StartupFinance newStartupFinance = StartupFinance.builder()
            .startup(newStartup)
            .revenue(request.revenue())
            .profit(request.profit())
            .fundingRounds(request.fundingRounds())
            .totalFunding(request.totalFunding())
            .investorStages(FundingStage.valueOf(request.investorStages().toUpperCase()))
            .build();

        newStartup.addStartupFinance(newStartupFinance);

        Startup savedStartup = startupRepository.save(newStartup);

        List<BusinessArea> businessAreas = businessAreaRepository.findAllByNameIn(
            request.businessAreas());

        List<StartupBusinessArea> businessAreaList = businessAreas.stream()
            .map(area -> StartupBusinessArea.builder()
                .id(new StartupBusinessAreaKey(savedStartup.getStartupId(), area.getId()))
                .startup(savedStartup)
                .businessArea(area)
                .build())
            .toList();
        startupBusinessAreaRepository.saveAll(businessAreaList);
       return new StartupProfileCreateResponse(savedStartup.getStartupId());

    }
    @Transactional
    public void updateStartupProfile(Long userId, StartupProfileUpdateRequest request){
        User owner = userFinder.findByUserId(userId);
        Startup startup = startupRepository.findByOwner(owner)
            .orElseThrow(() -> new CustomException(ErrorCode.STARTUP_NOT_FOUND));
        startup.update(request);
        startup.getStartupFinance().update(request);
        startup.getStartupProfile().update(request);
        if(request.businessAreas() != null){
            updateBusinessAreas(startup,request.businessAreas());
        }

    }

    private void updateBusinessAreas(Startup startup, List<String> areas) {

        startupBusinessAreaRepository.deleteAllByStartup_StartupId(startup.getStartupId());
        List<BusinessArea> businessAreas = businessAreaRepository.findAllByNameIn(areas);

        List<StartupBusinessArea> businessAreaList = businessAreas.stream()
            .map(area -> StartupBusinessArea.builder()
                .id(new StartupBusinessAreaKey(startup.getStartupId(), area.getId()))
                .startup(startup)
                .businessArea(area)
                .build())
            .toList();
        startupBusinessAreaRepository.saveAll(businessAreaList);
    }

    public StartupProfileInfo findStartupProfile(Long userId) {
        User owner = userFinder.findByUserId(userId);
        Startup startup = startupFinder.findByOwner(owner);

        List<StartupBusinessArea> startupBusinessAreaList = startupBusinessAreaRepository.findByStartup(startup);

        List<String> startupBusinessAreaNames = startupBusinessAreaList.stream()
            .map(area -> area.getBusinessArea().getName()).toList();

        return  StartupProfileInfo.toStartupProfileInfo(startup,startupBusinessAreaNames);
    }
}
