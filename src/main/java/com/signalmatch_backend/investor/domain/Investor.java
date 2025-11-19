package com.signalmatch_backend.investor.domain;


import com.signalmatch_backend.common.domain.BaseEntity;
import com.signalmatch_backend.investor.domain.enums.InvestmentSize;
import com.signalmatch_backend.investor.domain.enums.InvestorType;
import com.signalmatch_backend.investor.dto.InvestorProfileUpdateRequest;
import com.signalmatch_backend.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "investors")
public class Investor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long investorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;                  // 만든 사용자

    @Column(nullable = false, length = 100)
    private String investorName;

    @Email
    @Column(nullable = false, unique = true)
    private String contactEmail;

    private String phoneNumber;
    private String websiteUrl;

    @Lob
    private String intro;

    private String organizationName; //소속된 회사/기관 이름

    @Column(name = "views")
    private Long views; //조회수(플랫폼 내 투자자 프로필이 열람된 횟수)


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestorType investorType;

    @Enumerated(EnumType.STRING)
    private InvestmentSize preferredInvestmentSize;

    /** 선호 투자 단계 (SEED, SERIES_A, ...) */
    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvestorPreferredStage> preferredStages = new ArrayList<>();

    public void addPreferredStage(InvestorPreferredStage stage) {
        preferredStages.add(stage);
        stage.setInvestor(this);
    }
    public void update(InvestorProfileUpdateRequest request){
        if(request.investorName() !=null){
            this.investorName = request.investorName();
        }
        if(request.contactEmail() != null){
            this.contactEmail= request.contactEmail();
        }
        if(request.phoneNumber() != null ){
            this.phoneNumber = request.phoneNumber();
        }
        if(request.websiteUrl() != null ){
            this.websiteUrl = request.websiteUrl();
        }
        if(request.intro() != null ){
            this.intro = request.intro();
        }
        if(request.organizationName() != null ){
            this.organizationName = request.organizationName();
        }
        if(request.preferredInvestmentSize() != null){
            this.preferredInvestmentSize = InvestmentSize.valueOf(request.preferredInvestmentSize());
        }
        if(request.investorType() != null){
            this.investorType= InvestorType.valueOf(request.investorType());
        }
    }

}