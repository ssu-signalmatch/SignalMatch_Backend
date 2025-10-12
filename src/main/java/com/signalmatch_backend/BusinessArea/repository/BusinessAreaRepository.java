package com.signalmatch_backend.BusinessArea.repository;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessAreaRepository extends JpaRepository<BusinessArea,Long> {
    List<BusinessArea> findAllByNameIn(List<String> names);

}
