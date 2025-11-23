package com.signalmatch_backend.chat.domain;

import com.signalmatch_backend.user.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor
public class ChatMessage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private UserRole senderRole;  // STARTUP / INVESTOR

    private Long senderId;              // 실제 사용자 PK

    @Column(nullable = false, length = 1000)
    private String content;


    private boolean deleted = false;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.content = "(삭제된 메시지입니다)";
    }

    public ChatMessage(ChatRoom room, UserRole senderRole, Long senderId, String content) {
        this.chatRoom = room;
        this.senderRole = senderRole;
        this.senderId = senderId;
        this.content = content;
    }

}
