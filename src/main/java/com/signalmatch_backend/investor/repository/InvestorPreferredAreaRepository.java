package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredAreaKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorPreferredAreaRepository extends JpaRepository<InvestorPreferredArea, InvestorPreferredAreaKey> {
    void deleteAllByInvestor_InvestorId(Long investorId);

    List<InvestorPreferredArea> findByInvestor(Investor investor);
}
