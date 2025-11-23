package com.signalmatch_backend.chat.service;


import com.signalmatch_backend.chat.ChatMessageFinder;
import com.signalmatch_backend.chat.ChatRoomFinder;
import com.signalmatch_backend.chat.domain.ChatMessage;
import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.chat.dto.ChatMessageRequest;
import com.signalmatch_backend.chat.dto.ChatMessageResponse;
import com.signalmatch_backend.chat.repository.ChatMessageRepository;
import com.signalmatch_backend.chat.repository.ChatRoomRepository;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.user.domain.enums.UserRole;
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
    private final ChatMessageFinder chatMessageFinder;

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
            Long senderUserId,
            UserRole senderRole,
            String content
    ) {
        ChatRoom room = getOrCreateRoom(startupId, investorId);

        validateSender(room, senderRole, senderUserId);

        ChatMessage message = new ChatMessage(
                room,
                senderRole,
                senderUserId,
                content
        );

        ChatMessage saved = chatMessageRepository.save(message);
        return ChatMessageResponse.from(saved);
    }


    private void validateSender(ChatRoom room, UserRole role, Long senderId) {
        if (role == UserRole.STARTUP && !room.getStartupId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }

        if (role == UserRole.INVESTOR && !room.getInvestorId().equals(senderId)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }
    }



    @Transactional
    public void deleteMessage(Long messageId, Long requesterUserId, UserRole requesterRole) {

        ChatMessage message = chatMessageFinder.findById(messageId);

        // 본인 메시지인지 확인
        if (!message.getSenderId().equals(requesterUserId)
                || !message.getSenderRole().equals(requesterRole)) {
            throw new CustomException(ErrorCode.CHAT_SENDER_INVALID);
        }

        // soft delete
        message.softDelete();
    }
}
