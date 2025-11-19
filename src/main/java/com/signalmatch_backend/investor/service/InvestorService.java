package com.signalmatch_backend.investor.service;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.BusinessArea.repository.BusinessAreaRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.InvestorFinder;
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
import com.signalmatch_backend.investor.dto.InvestorProfileInfo;
import com.signalmatch_backend.investor.dto.InvestorProfileUpdateRequest;
import com.signalmatch_backend.investor.repository.InvestorPreferredAreaRepository;
import com.signalmatch_backend.investor.repository.InvestorPreferredStageRepository;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
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
    private final InvestorPreferredStageRepository investorPreferredStageRepository;
    private final InvestorFinder investorFinder;
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
    @Transactional
    public void updateInvestorProfile(Long userId, InvestorProfileUpdateRequest request){
        User owner = userFinder.findByUserId(userId);
        Investor investor = investorRepository.findByOwner(owner).orElseThrow(() -> new CustomException(ErrorCode.INVESTOR_NOT_FOUND));
        investor.update(request);
        if(request.preferredStages() != null){

            updatePreferredStages(investor,request.preferredStages());
        }
        if(request.preferredAreas() != null){
            updatePreferredAreas(investor,request.preferredAreas());
        }

    }

    private void updatePreferredAreas(Investor investor, List<String> areas) {
        investorPreferredAreaRepository.deleteAllByInvestor_InvestorId(investor.getInvestorId());

        List<BusinessArea> businessAreas = businessAreaRepository.findAllByNameIn(areas);

        List<InvestorPreferredArea> preferredAreas = businessAreas.stream()
            .map(area -> InvestorPreferredArea.builder()
                .id(new InvestorPreferredAreaKey(investor.getInvestorId(), area.getId()))
                .investor(investor)
                .businessArea(area)
                .build())
            .toList();

        investorPreferredAreaRepository.saveAll(preferredAreas);
    }

    private void updatePreferredStages(Investor investor, List<String> stages) {
        investorPreferredStageRepository.deleteAllByInvestor_InvestorId(investor.getInvestorId());

        List<InvestorPreferredStage> newPreferredStages = stages.stream()
            .map(stageString -> {
                StageCode stageEnum = StageCode.valueOf(stageString.toUpperCase());
                return InvestorPreferredStage.builder()
                    .id(new InvestorPreferredStageKey(investor.getInvestorId(), stageEnum))
                    .investor(investor)
                    .build();
            })
            .toList();
        investorPreferredStageRepository.saveAll(newPreferredStages);
    }
    
    public InvestorProfileInfo findInvestorProfile(Long userId){
        User owner= userFinder.findByUserId(userId);
        Investor investor = investorFinder.findByOwner(owner);

        List<InvestorPreferredArea> investorPreferredAreaList= investorPreferredAreaRepository.findByInvestor(investor);

        List<String> investorPreferredAreaNames = investorPreferredAreaList.stream()
            .map(area -> area.getBusinessArea().getName())
            .toList();

        return  InvestorProfileInfo.toInvestorProfileInfo(investor, investorPreferredAreaNames);
    }
}
