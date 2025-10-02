package com.signalmatch_backend.startup.domain;

import com.signalmatch_backend.common.domain.BaseEntity;
import com.signalmatch_backend.startup.domain.enums.StartupStatus;
import com.signalmatch_backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "startups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Startup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long startupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String startupName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StartupStatus status;

    @Column(nullable = false)
    private long views; //조회 수

}
