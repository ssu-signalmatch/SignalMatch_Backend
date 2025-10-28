package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.startup.domain.enums.LegalType;
import com.signalmatch_backend.startup.domain.enums.ScaleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "startup_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StartupProfile {

    @Id
    @Column(name = "startup_id")
    private Long id; // startups.startup_id와 동일

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "startup_id")
    private Startup startup;

    private LocalDate foundingDate;
    @Column(length = 255) private String address;
    @Column(length = 2048) private String homepageUrl;

    @Email @Column(length = 255)  private String contactEmail;

    @Lob private String intro;

    @Column(length = 50)   private String representativeName;
    @Column(length = 50)   private String businessNumber;

    private Integer employeeCount;

    @Enumerated(EnumType.STRING) @Column(length = 10)
    private LegalType legalType;

    @Enumerated(EnumType.STRING) @Column(length = 10)
    private ScaleType scale;
}