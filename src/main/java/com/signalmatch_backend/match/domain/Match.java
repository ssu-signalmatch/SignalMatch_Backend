package com.signalmatch_backend.match.domain;


import com.signalmatch_backend.common.domain.BaseEntity;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.user.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Match extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Column(nullable = false)
    private Long startupId;

    @Column(nullable = false)
    private Long investorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status;

    @Column
    private LocalDateTime matchedAt;

    // 거절/취소/종료 시각
    @Column
    private LocalDateTime endedAt;

    //누가 요청했는지(STARTUP or INVESTOR)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole requestedBy;

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setMatchedAt(LocalDateTime matchedAt) { this.matchedAt = matchedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }
}
