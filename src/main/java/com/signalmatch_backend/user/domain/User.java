package com.signalmatch_backend.user.domain;

import com.signalmatch_backend.common.domain.BaseEntity;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.user.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole userRole;

    @OneToOne(mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Startup startup;

    @OneToOne(mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Investor investor;
}