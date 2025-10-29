package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.startup.domain.enums.LegalType;
import com.signalmatch_backend.startup.domain.enums.ScaleType;
import com.signalmatch_backend.startup.dto.StartupProfileUpdateRequest;
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
    // StartupProfile.java

    public void update(StartupProfileUpdateRequest request) {
        if (request.foundingDate() != null) {
            this.foundingDate = request.foundingDate();
        }
        if (request.address() != null) {
            this.address = request.address();
        }
        if (request.homepageUrl() != null) {
            this.homepageUrl = request.homepageUrl();
        }
        if (request.contactEmail() != null) {
             this.contactEmail = request.contactEmail();
        }
        if (request.intro() != null ) {
            this.intro = request.intro();
        }
        if (request.representativeName() != null) {
            this.representativeName = request.representativeName();
        }
        if (request.businessNumber() != null ) {
            this.businessNumber = request.businessNumber();
        }
        if (request.employeeCount() != null) {
            this.employeeCount = request.employeeCount();
        }
        if (request.legalType() != null ) {
            this.legalType = LegalType.valueOf(request.legalType().toUpperCase());
        }
        if (request.scale() != null ) {
            this.scale = ScaleType.valueOf(request.scale().toUpperCase());
        }
    }
}