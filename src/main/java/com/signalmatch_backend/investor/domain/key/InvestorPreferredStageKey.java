package com.signalmatch_backend.investor.domain.key;

import com.signalmatch_backend.investor.domain.enums.StageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestorPreferredStageKey implements Serializable {
    private Long investorId;
    private StageCode stageCode;
}
