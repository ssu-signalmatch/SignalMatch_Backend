package com.signalmatch_backend.investor.domain.key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvestorPreferredAreaKey implements Serializable {
    private Long investorId;
    private Long businessAreaId;
}