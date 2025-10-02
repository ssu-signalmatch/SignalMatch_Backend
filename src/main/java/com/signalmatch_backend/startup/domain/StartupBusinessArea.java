package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.BusinessArea.domain.BusinessArea;
import com.signalmatch_backend.startup.domain.key.StartupBusinessAreaKey;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "startups_business_area")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class StartupBusinessArea {

    @EmbeddedId
    private StartupBusinessAreaKey id;

    @MapsId("startupId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id")
    private Startup startup;

    @MapsId("businessAreaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_area_id")
    private BusinessArea businessArea;
}