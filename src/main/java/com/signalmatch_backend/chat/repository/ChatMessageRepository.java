package com.signalmatch_backend.chat.repository;

import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.user.domain.enums.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 방의 메시지를 오래된 순으로 size개
    List<ChatMessage> findByChatRoomIdAndDeletedFalseOrderByIdAsc(Long chatRoomId, Pageable pageable);

    // 마지막 메시지 이후 것만 (polling용)
    List<ChatMessage> findByChatRoomIdAndIdGreaterThanAndDeletedFalseOrderByIdAsc(
            Long chatRoomId,
            Long lastMessageId
    );


    // 특정 채팅방의 마지막 메시지 (id 기반 최신)
    Optional<ChatMessage> findTopByChatRoomIdAndDeletedFalseOrderByIdDesc(Long chatRoomId);

    // 특정 채팅방에서, 상대가 보냈고 아직 읽지 않은 메시지 수 (STARTUP 시점)
    long countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByStartupFalse(
            Long chatRoomId,
            UserRole senderRoleNot
    );

    // INVESTOR 시점
    long countByChatRoomIdAndDeletedFalseAndSenderRoleNotAndReadByInvestorFalse(
            Long chatRoomId,
            UserRole senderRoleNot
    );
}
