package com.signalmatch_backend.chat.domain;

import com.signalmatch_backend.match.domain.Match;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_match_chat_room_match", columnNames = "match_id")
        })
public class ChatRoom {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startup_id", nullable = false)
    private Long startupId;

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public ChatRoom(Long startupId, Long investorId) {
        this.startupId = startupId;
        this.investorId = investorId;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
