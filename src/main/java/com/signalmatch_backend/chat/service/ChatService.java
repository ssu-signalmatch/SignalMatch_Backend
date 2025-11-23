package com.signalmatch_backend.chat.service;


import com.signalmatch_backend.chat.ChatRoomFinder;
import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.chat.domain.enums.ChatSenderRole;
import com.signalmatch_backend.chat.dto.ChatMessageRequest;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.repository.ChatMessageRepository;
import com.signalmatch_backend.chat.repository.ChatRoomRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomFinder chatRoomFinder;

    @Transactional
    public ChatRoom getOrCreateRoom(Long startupId, Long investorId) {
        return chatRoomRepository.findByStartupIdAndInvestorId(startupId, investorId)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoom(startupId, investorId)));
    }

    //메시지 전송
    @Transactional
    public ChatMessageResponse sendMessage(
            Long startupId,
            Long investorId,
            ChatMessageRequest request
    ) {
        ChatRoom room = getOrCreateRoom(startupId, investorId);

        validateSender(room, request.senderRole(), request.senderId());

        ChatMessage message = new ChatMessage(
                room,
                request.senderRole(),
                request.senderId(),
                request.content()
        );

        ChatMessage saved = chatMessageRepository.save(message);
        return ChatMessageResponse.from(saved);
    }


    private void validateSender(ChatRoom room, ChatSenderRole role, Long senderId) {
        if (role == ChatSenderRole.STARTUP && !room.getStartupId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }

        if (role == ChatSenderRole.INVESTOR && !room.getInvestorId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }
    }
}
