package com.signalmatch_backend.investor.domain;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredAreaKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "investors_preferred_areas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InvestorPreferredArea {

    @EmbeddedId
    private InvestorPreferredAreaKey id;

    @MapsId("investorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id")
    private Investor investor;

    @MapsId("businessAreaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_area_id")
    private BusinessArea businessArea;
}