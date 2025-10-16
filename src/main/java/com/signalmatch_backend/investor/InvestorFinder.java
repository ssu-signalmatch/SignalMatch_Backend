package com.signalmatch_backend.investor;


import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.repository.InvestorRepository;
import com.signalmatch_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvestorFinder
{
    private final InvestorRepository investorRepository;

    public Investor findByInvestorId(long investorId) {
        return investorRepository.findById(investorId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVESTOR_NOT_FOUND));
    }

    public Investor findByOwner(User user) {
        return investorRepository.findByOwner(user)
                .orElseThrow(() -> new CustomException(ErrorCode.INVESTOR_NOT_FOUND));
    }
}
