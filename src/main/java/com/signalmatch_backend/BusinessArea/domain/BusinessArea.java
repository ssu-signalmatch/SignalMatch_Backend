package com.signalmatch_backend.BusinessArea.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_areas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BusinessArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;
}