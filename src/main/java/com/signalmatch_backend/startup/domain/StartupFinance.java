package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.startup.domain.enums.FundingStage;
import com.signalmatch_backend.startup.dto.StartupProfileUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "startup_finance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StartupFinance {

    @Id
    @Column(name = "startup_id")
    private Long id; // startups.startup_id와 동일
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "startup_id")
    private Startup startup;

    private Long revenue; //매출액
    private Long profit;  //영업 이익
    private Integer fundingRounds; //투자 유치 건수
    private Long totalFunding;     //누적 투자 유지 금액

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FundingStage investorStages; // 투자 단계
    // StartupFinance.java

    public void update(StartupProfileUpdateRequest request) {
        if (request.revenue() != null) {
            this.revenue = request.revenue();
        }
        if (request.profit() != null) {
            this.profit = request.profit();
        }
        if (request.fundingRounds() != null) {
            this.fundingRounds = request.fundingRounds();
        }
        if (request.totalFunding() != null) {
            this.totalFunding = request.totalFunding();
        }
        if (request.investorStages() != null) {
            this.investorStages = FundingStage.valueOf(request.investorStages().toUpperCase());
        }
    }
}