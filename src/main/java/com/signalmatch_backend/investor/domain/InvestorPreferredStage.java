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

    @Id
    @Column(name = "stage_code", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StageCode stageCode; // SEED, SERIES_A, SERIES_B, SERIES_C ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id")
    private Investor investor;
}