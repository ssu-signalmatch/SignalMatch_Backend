package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.InvestorPreferredStage;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredStageKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorPreferredStageRepository extends JpaRepository<InvestorPreferredStage , InvestorPreferredStageKey> {
    void deleteAllByInvestor_InvestorId(Long investorID);
}
