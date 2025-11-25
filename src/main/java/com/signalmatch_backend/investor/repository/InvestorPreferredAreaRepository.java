package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredAreaKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestorPreferredAreaRepository extends JpaRepository<InvestorPreferredArea, InvestorPreferredAreaKey> {
    void deleteAllByInvestor_InvestorId(Long investorId);

    List<InvestorPreferredArea> findByInvestor(Investor investor);
    @Query("SELECT ipa " +
        "FROM InvestorPreferredArea ipa " +
        "JOIN FETCH ipa.businessArea " +
        "WHERE ipa.investor.investorId IN :investorIds")
    List<InvestorPreferredArea> findAllByInvestorIds(@Param("investorIds") List<Long> investorIds);;
}
