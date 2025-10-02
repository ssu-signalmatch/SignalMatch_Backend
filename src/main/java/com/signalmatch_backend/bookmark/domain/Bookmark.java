package com.signalmatch_backend.bookmark.domain;

import com.signalmatch_backend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookmarks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","investor_id","startup_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @Column(nullable = false) private Long userId;
    @Column private Long investorId; // 둘 중 하나만 값이 있도록 서비스 레벨에서 보장
    @Column private Long startupId;
}