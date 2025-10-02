package com.signalmatch_backend.investor.domain;

import com.signalmatch_backend.investor.domain.enums.StageCode;
import com.signalmatch_backend.investor.domain.key.InvestorPreferredStageKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "investment_preferred_stages")
public class InvestorPreferredStage {
    @EmbeddedId
    private InvestorPreferredStageKey id;

    @MapsId("investorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

}