package com.signalmatch_backend.chat;

import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.repository.ChatMessageRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageFinder {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage findById(Long messageId) {
        return chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATMESSAGE_NOT_FOUND));
    }
}
