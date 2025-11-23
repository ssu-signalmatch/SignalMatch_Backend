package com.signalmatch_backend.chat.repository;

import com.signalmatch_backend.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage>
    findByChatRoomIdAndDeletedFalseOrderByIdAsc(Long chatRoomId);

    List<ChatMessage>
    findByChatRoomIdAndIdGreaterThanAndDeletedFalseOrderByIdAsc(Long chatRoomId, Long lastMessageId);
}
