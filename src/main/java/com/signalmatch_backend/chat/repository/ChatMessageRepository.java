package com.signalmatch_backend.chat.repository;

import com.signalmatch_backend.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 방의 메시지를 오래된 순으로 size개
    List<ChatMessage> findByChatRoomIdAndDeletedFalseOrderByIdAsc(Long chatRoomId, Pageable pageable);

    // 마지막 메시지 이후 것만 (polling용)
    List<ChatMessage> findByChatRoomIdAndIdGreaterThanAndDeletedFalseOrderByIdAsc(
            Long chatRoomId,
            Long lastMessageId
    );
}
