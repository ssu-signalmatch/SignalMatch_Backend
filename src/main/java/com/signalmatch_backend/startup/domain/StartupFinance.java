package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.startup.domain.enums.FundingStage;
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

}