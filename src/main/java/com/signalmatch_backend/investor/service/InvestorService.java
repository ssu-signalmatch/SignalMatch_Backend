package com.signalmatch_backend.investor.service;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.BusinessArea.repository.BusinessAreaRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.investor.domain.InvestorPreferredStage;
import com.signalmatch_backend.investor.domain.enums.InvestmentSize;
import com.signalmatch_backend.investor.domain.enums.InvestorType;
import com.signalmatch_backend.investor.domain.enums.StageCode;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredAreaKey;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredStageKey;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateRequest;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateResponse;
import com.signalmatch_backend.investor.repository.InvestorPreferredAreaRepository;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvestorService {
    private final InvestorRepository investorRepository;
    private final UserFinder userFinder;
    private final BusinessAreaRepository businessAreaRepository;
    private final InvestorPreferredAreaRepository investorPreferredAreaRepository;
    @Transactional
    public InvestorProfileCreateResponse createInvestorProfile(Long userId, InvestorProfileCreateRequest request){
        User owner = userFinder.findByUserId(userId);

        if(investorRepository.existsByOwner(owner)){
            throw new CustomException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        Investor newInvestor=Investor.builder()
            .owner(owner)
            .investorName(request.investorName())
            .contactEmail(request.contactEmail())
            .position(request.position())
            .phoneNumber(request.phoneNumber())
            .websiteUrl(request.websiteUrl())
            .intro(request.intro())
            .organizationName(request.organizationName())
            .investorType(InvestorType.valueOf(request.investorType().toUpperCase()))
            .preferredInvestmentSize(InvestmentSize.valueOf(request.preferredInvestmentSize().toUpperCase()))
            .views((long) 0L)
            .build();

        request.preferredStages().forEach(stage -> {
            StageCode stageEnum= StageCode.valueOf(stage.toUpperCase());

            InvestorPreferredStageKey key = new InvestorPreferredStageKey(null,stageEnum);

            InvestorPreferredStage preferredStage= InvestorPreferredStage.builder()
                .id(key)
                .build();
            newInvestor.addPreferredStage(preferredStage);
        });

        Investor savedInvestor = investorRepository.save(newInvestor);

        List<BusinessArea> businessAreas = businessAreaRepository.findAllByNameIn(
            request.preferredAreas());

        List<InvestorPreferredArea> preferredAreas = businessAreas.stream()
            .map(area -> InvestorPreferredArea.builder()
                .id(new InvestorPreferredAreaKey(savedInvestor.getInvestorId(), area.getId()))
                .investor(savedInvestor)
                .businessArea(area)
                .build())
            .toList();

        investorPreferredAreaRepository.saveAll(preferredAreas);

        return new InvestorProfileCreateResponse(savedInvestor.getInvestorId());
        
    }
}
