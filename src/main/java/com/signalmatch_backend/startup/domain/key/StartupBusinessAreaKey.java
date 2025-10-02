package com.signalmatch_backend.startup.domain.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartupBusinessAreaKey implements Serializable {
    private Long startupId;
    private Long businessAreaId;
}